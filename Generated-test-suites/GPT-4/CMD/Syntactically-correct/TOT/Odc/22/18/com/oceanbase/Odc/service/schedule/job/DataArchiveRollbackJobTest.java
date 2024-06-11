package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob job;
    private ScheduleTaskRepository mockRepo;
    private JobExecutionContext mockContext;

    @Before
    public void setup() {
        mockRepo = mock(ScheduleTaskRepository.class);
        mockContext = mock(JobExecutionContext.class);
        job = new DataArchiveRollbackJob(mockRepo);
    }

    @Test
    public void testExecute() {
        // create test data and setup mock behaviors
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"DataArchiveTaskId\": 1}");
        when(mockContext.getResult()).thenReturn(taskEntity);
        when(mockRepo.findById(any())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(mockRepo.updateStatusById(any(),any())).thenReturn(1);
        job.execute(mockContext);
        verify(mockRepo, times(1)).updateStatusById(any(), equals(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_noDataArchiveTask() {
        // create test data and setup mock behaviors
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"DataArchiveTaskId\": 1}");
        when(mockContext.getResult()).thenReturn(taskEntity);
        when(mockRepo.findById(any())).thenReturn(Optional.empty());
        when(mockRepo.updateStatusById(any(),any())).thenReturn(1);
        job.execute(mockContext);
        verify(mockRepo, times(1)).updateStatusById(any(), equals(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecute_taskStatusNotPreparing() {
        // create test data and setup mock behaviors
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"DataArchiveTaskId\": 1}");
        when(mockContext.getResult()).thenReturn(taskEntity);

        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(dlmTask)));
        when(mockRepo.findById(any())).thenReturn(Optional.of(dataArchiveTask));

        when(mockRepo.updateStatusById(any(),any())).thenReturn(1);
        job.execute(mockContext);
        assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
    }

    //TODO Add more test cases for edge cases and error scenarios if needed
}