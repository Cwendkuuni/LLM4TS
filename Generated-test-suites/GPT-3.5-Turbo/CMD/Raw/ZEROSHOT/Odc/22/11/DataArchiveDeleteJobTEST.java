package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
        taskEntity.setResultJson("[{\"id\":1,\"status\":\"DONE\"}]");
    }

    @Test
    public void testExecuteWithValidTask() {
        dataArchiveDeleteJob.execute(context);
        Optional<ScheduleTaskEntity> taskOption = dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L);
        assertTrue(taskOption.isPresent());
        assertEquals(TaskStatus.DONE, taskOption.get().getStatus());
    }

    @Test
    public void testExecuteWithInvalidTask() {
        taskEntity.setResultJson("[]");
        dataArchiveDeleteJob.execute(context);
        Optional<ScheduleTaskEntity> taskOption = dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L);
        assertTrue(taskOption.isPresent());
        assertEquals(TaskStatus.FAILED, taskOption.get().getStatus());
    }

    @Test
    public void testExecuteWithDifferentStatusTask() {
        taskEntity.getResultJson("[{\"id\":1,\"status\":\"FAILED\"}]");
        dataArchiveDeleteJob.execute(context);
        Optional<ScheduleTaskEntity> taskOption = dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L);
        assertTrue(taskOption.isPresent());
        assertEquals(TaskStatus.FAILED, taskOption.get().getStatus());
    }

    @Test
    public void testExecuteWithInvalidDataArchiveTask() {
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":2}");
        dataArchiveDeleteJob.execute(context);
        Optional<ScheduleTaskEntity> taskOption = dataArchiveDeleteJob.getScheduleTaskRepository().findById(1L);
        assertTrue(taskOption.isPresent());
        assertEquals(TaskStatus.FAILED, taskOption.get().getStatus());
    }
}