
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob job;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        job = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        rollbackParameters = mock(DataArchiveRollbackParameters.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        job.setScheduleTaskRepository(scheduleTaskRepository);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("rollbackParametersJson");
    }

    @Test
    public void testExecute() {

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask[]{ new DlmTask() });
        taskUnits.forEach(taskUnit -> {
            Long temp = taskUnit.getSourceDatabaseId();
            taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                    taskEntity.getId(),
                    taskUnits.size()));
            taskUnit.setSourceDatabaseId(taskUnit.getTargetDatabaseId());
            taskUnit.setTargetDatabaseId(temp);
            taskUnit.setJobType(JobType.ROLLBACK);
            taskUnit.setStatus(taskUnit.getStatus() == TaskStatus.PREPARING ? TaskStatus.DONE : TaskStatus.PREPARING);
        });

        job.execute(context);

        assertEquals("taskUnits should be prepared for rollback", taskUnits, job.getTaskUnits());
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), taskStatus);
    }
}
