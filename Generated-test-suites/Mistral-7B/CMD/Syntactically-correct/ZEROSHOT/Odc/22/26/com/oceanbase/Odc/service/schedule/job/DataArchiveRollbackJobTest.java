package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob job;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        job = new DataArchiveRollbackJob();
        taskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-existing-id");

        when(scheduleTaskRepository.findById(eq("non-existing-id"))).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        job.execute(context);

        verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_dataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        when(scheduleTaskRepository.findById(eq("existing-id"))).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setMergedJobDataMap(map -> map.put("taskEntity", taskEntity));

        when(scheduleTaskRepository.findById(eq("existing-id"))).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(eq(dataArchiveTask.getResultJson()), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        try {
            job.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
    }

    @Test
    public void testGetTaskStatus_allTasksDone() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask().setStatus(TaskStatus.DONE),
                new DlmTask().setStatus(TaskStatus.DONE));
        TaskStatus expected = TaskStatus.DONE;

        TaskStatus actual = job.getTaskStatus(taskUnits);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTaskStatus_tasksPreparing() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask().setStatus(TaskStatus.PREPARING),
                new DlmTask().setStatus(TaskStatus.PREPARING));
        TaskStatus expected = TaskStatus.PREPARING;

        TaskStatus actual = job.getTaskStatus(taskUnits);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTaskStatus_tasksFailed() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask().setStatus(TaskStatus.FAILED),
                new DlmTask().setStatus(TaskStatus.FAILED));
        TaskStatus expected = TaskStatus.FAILED;

        TaskStatus actual = job.getTaskStatus(taskUnits);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTaskStatus_tasksMixed() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask().setStatus(TaskStatus.DONE),
                new DlmTask().setStatus(TaskStatus.FAILED), new DlmTask().setStatus(TaskStatus.PREPARING));
        TaskStatus expected = TaskStatus.FAILED;

        TaskStatus actual = job.getTaskStatus(taskUnits);

        assertEquals(expected, actual);
    }
}