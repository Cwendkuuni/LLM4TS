import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionResult;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jobstore.mock.MockJobStore;
import org.quartz.impl.quartz.JobBuilder;
import org.quartz.impl.quartz.trigger.MockTrigger;
import org.quartz.impl.quartz.JobDetail;
import org.quartz.impl.quartz.SchedulerImpl;
import org.quartz.impl.quartz.Trigger;
import org.quartz.impl.quartz.jobStore.JobStoreSupport;
import org.quartz.impl.quartz.jobStore.JobStoreTX;
import org.quartz.impl.quartz.jobStore.LockSupport;
import org.quartz.impl.quartz.SchedulerContext;
import org.quartz.impl.quartz.SchedulerContextEntry;
import org.quartz.impl.quartz.SchedulerExecutionContext;
import org.quartz.impl.quartz.spi.JobFactory;
import org.quartz.impl.quartz.JobInitializationException;
import org.quartz.impl.quartz.JobExecutionException;
import org.quartz.impl.quartz.ObjectAlreadyExistsException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private SchedulerImpl scheduler;
    private StdSchedulerFactory schedulerFactory;
    private MockJobStore jobStore;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        schedulerFactory = new StdSchedulerFactory();
        jobStore = new MockJobStore();
        scheduler = (SchedulerImpl) schedulerFactory.getScheduler();
        scheduler.setJobStore(jobStore);
        scheduler.setJobFactory(new JobFactory() {
            @Override
            public Object instantiate(Class clazz, Object arg) throws InstantiationException, IllegalAccessException,
                    JobInitializationException {
                return dataArchiveRollbackJob;
            }
        });
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        // Given
        String taskJson = "{\"id\": 1, \"jobName\": \"testJobName\", \"jobGroup\": \"testJobGroup\", \"parametersJson\": {\"dataArchiveTaskId\": \"123\"}, \"resultJson\": \"[]\"}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("1");
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("123")));
        taskEntity.setResultJson(taskJson);

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

        // When
        JobExecutionContext context = new JobExecutionContext();
        context.setResult(new MockitoJobExecutionResult());
        context.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context.setJobInstance(mock(JobInstance.class));
        context.setCause(null);
        context.setFireTime(null);
        context.setJobDataMap(new JobDataMap());
        context.setJobKey(mock(JobKey.class));
        context.setSchedulerContext(new SchedulerContext());
        context.setScheduler(scheduler);

        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // Given
        String taskJson = "{\"id\": 1, \"jobName\": \"testJobName\", \"jobGroup\": \"testJobGroup\", \"parametersJson\": {\"dataArchiveTaskId\": \"123\"}, \"resultJson\": \"[]\"}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("1");
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("123")));
        taskEntity.setResultJson(taskJson);

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        JobExecutionContext context = new JobExecutionContext();
        context.setResult(new MockitoJobExecutionResult());
        context.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context.setJobInstance(mock(JobInstance.class));
        context.setCause(null);
        context.setFireTime(null);
        context.setJobDataMap(new JobDataMap());
        context.setJobKey(mock(JobKey.class));
        context.setSchedulerContext(new SchedulerContext());
        context.setScheduler(scheduler);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskExecutionSuccess() throws Exception {
        // Given
        String taskJson = "{\"id\": 1, \"jobName\": \"testJobName\", \"jobGroup\": \"testJobGroup\", \"parametersJson\": {\"dataArchiveTaskId\": \"123\"}, \"resultJson\": \"[]\"}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("1");
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("123")));
        taskEntity.setResultJson(taskJson);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

        // When
        JobExecutionContext context = new JobExecutionContext();
        context.setResult(new MockitoJobExecutionResult());
        context.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context.setJobInstance(mock(JobInstance.class));
        context.setCause(null);
        context.setFireTime(null);
        context.setJobDataMap(new JobDataMap());
        context.setJobKey(mock(JobKey.class));
        context.setSchedulerContext(new SchedulerContext());
        context.setScheduler(scheduler);

        when(taskUnits.get(0).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(0).getTargetDatabaseId()).thenReturn(2L);
        when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.DONE);
        when(taskUnits.get(1).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getTargetDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskExecutionFailed() throws Exception {
        // Given
        String taskJson = "{\"id\": 1, \"jobName\": \"testJobName\", \"jobGroup\": \"testJobGroup\", \"parametersJson\": {\"dataArchiveTaskId\": \"123\"}, \"resultJson\": \"[]\"}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("1");
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("123")));
        taskEntity.setResultJson(taskJson);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

        // When
        JobExecutionContext context = new JobExecutionContext();
        context.setResult(new MockitoJobExecutionResult());
        context.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context.setJobInstance(mock(JobInstance.class));
        context.setCause(null);
        context.setFireTime(null);
        context.setJobDataMap(new JobDataMap());
        context.setJobKey(mock(JobKey.class));
        context.setSchedulerContext(new SchedulerContext());
        context.setScheduler(scheduler);

        when(taskUnits.get(0).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(0).getTargetDatabaseId()).thenReturn(2L);
        when(taskUnits.get(0).getStatus()).thenThrow(new JobExecutionException("Test exception", null));
        when(taskUnits.get(1).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getTargetDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getStatus()).thenReturn(TaskStatus.PREPARING);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_ConcurrentExecution() throws Exception {
        // Given
        String taskJson = "{\"id\": 1, \"jobName\": \"testJobName\", \"jobGroup\": \"testJobGroup\", \"parametersJson\": {\"dataArchiveTaskId\": \"123\"}, \"resultJson\": \"[]\"}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("1");
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("123")));
        taskEntity.setResultJson(taskJson);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

        // When
        JobExecutionContext context1 = new JobExecutionContext();
        context1.setResult(new MockitoJobExecutionResult());
        context1.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context1.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context1.setJobInstance(mock(JobInstance.class));
        context1.setCause(null);
        context1.setFireTime(null);
        context1.setJobDataMap(new JobDataMap());
        context1.setJobKey(mock(JobKey.class));
        context1.setSchedulerContext(new SchedulerContext());
        context1.setScheduler(scheduler);

        when(taskUnits.get(0).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(0).getTargetDatabaseId()).thenReturn(2L);
        when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.PREPARING);

        JobExecutionContext context2 = new JobExecutionContext();
        context2.setResult(new MockitoJobExecutionResult());
        context2.setMergedJobDataMap(new MockitoJobExecutionResult().getMergedJobDataMap());
        context2.setJobDetail(new JobDetail("testJobDetail", "", DataArchiveRollbackJob.class));
        context2.setJobInstance(mock(JobInstance.class));
        context2.setCause(null);
        context2.setFireTime(null);
        context2.setJobDataMap(new JobDataMap());
        context2.setJobKey(mock(JobKey.class));
        context2.setSchedulerContext(new SchedulerContext());
        context2.setScheduler(scheduler);

        when(taskUnits.get(1).getSourceDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getTargetDatabaseId()).thenReturn(1L);
        when(taskUnits.get(1).getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveRollbackJob.execute(context1);
        dataArchiveRollbackJob.execute(context2);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testGetTaskStatus_TasksExecutedSuccessfully() throws Exception {
        // Given
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_TasksExecuting() throws Exception {
        // Given
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.PREPARING);
        when(taskUnits.get(1).getStatus()).thenReturn(TaskStatus.PREPARING);

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_TasksExecutingFailed() throws Exception {
        // Given
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(taskUnits.get(0).getStatus()).thenThrow(new JobExecutionException("Test exception", null));

        // When
        try {
            TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);
        } catch (JobExecutionException e) {
            // When an exception is thrown, it means the test passed
        }

        // Then
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testGetTaskStatus_EmptyTaskList() throws Exception {
        // Given
        List<DlmTask> taskUnits = Arrays.asList();

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        assertEquals(TaskStatus.UNKNOWN, taskStatus);
    }
}