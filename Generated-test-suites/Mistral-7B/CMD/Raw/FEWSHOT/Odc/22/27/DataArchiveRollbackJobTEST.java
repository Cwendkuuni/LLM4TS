package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        taskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        try {
            dataArchiveRollbackJob.execute(context);
            assertFalse(taskEntity.getStatus().equals(TaskStatus.FAILED));
        } catch (JobExecutionException e) {
            // expected
        }

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setStatus(TaskStatus.FAILED);
        DlmTask taskUnit2 = new DlmTask();
        taskUnit2.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        TaskStatus expected = TaskStatus.DONE;
        TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(expected, actual);
    }
}