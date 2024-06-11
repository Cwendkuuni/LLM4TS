
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    @Test
    public void executeTest() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("1");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);

        DlmTask resultTask = taskUnits.get(0);
        assertEquals(JobType.ROLLBACK, resultTask.getJobType());
        assertEquals(TaskStatus.DONE, resultTask.getStatus());
    }
}
