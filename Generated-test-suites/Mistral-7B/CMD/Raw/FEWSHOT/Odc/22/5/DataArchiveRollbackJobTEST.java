package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            @Override
            ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            dataArchiveRollbackJob.execute(context);
            assertFalse(taskEntity.getStatus() == TaskStatus.RUNNING);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        taskUnits.add(dlmTask);

        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId(1L);

        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(expectedRollbackParameters).getBytes());
        when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        try {
            dataArchiveRollbackJob.execute(context);
            assertEquals(TaskStatus.RUNNING, taskEntity.getStatus());
        } catch (JobExecutionException e) {
            fail("Unexpected JobExecutionException");
        }

        // Verify that the taskUnits have been updated correctly
        assertEquals(TaskStatus.DONE, dlmTask.getStatus());
        assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
        assertEquals(taskEntity.getId(), dlmTask.getJobId());

        // Verify that the status has been updated in the repository
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}