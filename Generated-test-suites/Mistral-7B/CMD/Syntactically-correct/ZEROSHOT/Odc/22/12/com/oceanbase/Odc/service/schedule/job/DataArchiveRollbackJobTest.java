package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetailFactory;
import org.quartz.MockitoScheduler;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.MockitoCronTriggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private MockitoScheduler scheduler;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduler = new MockitoScheduler();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // When & Then
        doThrow(new JobExecutionException("Data archive task not found")).when(dataArchiveRollbackJob)
                .execute(any(JobExecutionContext.class));

        JobExecutionContext context = new MockitoJobDetailFactory()
                .newJob(DataArchiveRollbackJob.class, "jobName", "jobGroup")
                .build();
        scheduler.scheduleJob(context, new Trigger() {
            @Override
            public String getExpression() {
                return "0/1 * * * * ?";
            }

            @Override
            public String getName() {
                return "triggerName";
            }
        });
        scheduler.start();
        scheduler.runOnly();
        verify(log).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=1");
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
        scheduler.shutdown();
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        // Given
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("jobName");
        dataArchiveTask.setJobGroup("jobGroup");
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(2L);
        taskEntity.setParametersJson(objectMapper.writeValueAsString(new DataArchiveRollbackParameters()));
        taskEntity.setResultJson(objectMapper.writeValueAsString(getDlmTasks()));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(log, never()).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=1");
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), eq(TaskStatus.DONE));
    }

    private List<DlmTask> getDlmTasks() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask1);
        return dlmTasks;
    }
}