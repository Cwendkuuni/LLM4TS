
package com.oceanbase.odc.service.schedule.job;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class DataArchiveDeleteJobTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testExecuteJobWithValidScenario() throws JobExecutionException {
        // setup job execution context and task entity
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(UUID.randomUUID().toString());
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":\"testId\"}");
        taskEntity.setStatus(TaskStatus.FINISHED);

        Mockito.when(context.getResult()).thenReturn(taskEntity);

        // setup another task entity and return when queried
        ScheduleTaskEntity archiveTask = new ScheduleTaskEntity();
        archiveTask.setId("testId");
        archiveTask.setStatus(TaskStatus.DONE);
        Mockito.when(scheduleTaskRepository.findById(Mockito.any())).thenReturn(Optional.of(archiveTask));
        
        // setup list of DlmTasks and return when queried
        List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
        Mockito.when(JsonUtils.fromJson(Mockito.any(), Mockito.any())).thenReturn(tasks);
        
        // execute the job
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        // check status of job
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
        tasks.forEach(task -> {
            assertEquals(JobType.DELETE, task.getJobType());
            assertEquals(TaskStatus.PREPARING, task.getStatus());
        });
    }
    
    @Test
    public void testHandleNonExistentArchiveTask() throws JobExecutionException {
        // setup job execution context and task entity
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(UUID.randomUUID().toString());
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":\"testId\"}");
        taskEntity.setStatus(TaskStatus.FINISHED);

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        
        // execute the job
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        // check status of job
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
    
    @Test
    public void testHandleArchiveTaskNotDone() throws JobExecutionException {
        // setup job execution context and task entity
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(UUID.randomUUID().toString());
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":\"testId\"}");
        taskEntity.setStatus(TaskStatus.FINISHED);

        // setup another task entity and return when queried
        ScheduleTaskEntity archiveTask = new ScheduleTaskEntity();
        archiveTask.setId("testId");
        archiveTask.setStatus(TaskStatus.PREPARING);
        Mockito.when(scheduleTaskRepository.findById(Mockito.any())).thenReturn(Optional.of(archiveTask));
        
        // execute the job
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        // check status of job
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}
