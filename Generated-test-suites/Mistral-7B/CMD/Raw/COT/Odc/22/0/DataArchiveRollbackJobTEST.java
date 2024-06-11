import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;
import com.oceanbase.tools.migrator.common.utils.DlmTaskUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    // Test execute method with valid parameters
    @Test
    public void testExecuteWithValidParameters() {
        // Given
        String jsonString = "{\"dataArchiveTaskId\":1, \"parametersJson\":{\"key\":\"value\"}}";
        ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
        expectedScheduleTaskEntity.setId(1L);
        expectedScheduleTaskEntity.setParametersJson(jsonString);
        expectedScheduleTaskEntity.setJobName("jobName");
        expectedScheduleTaskEntity.setJobGroup("jobGroup");

        DataArchiveRollbackParameters expectedDataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        expectedDataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(expectedDataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(expectedScheduleTaskEntity));

        // When
        JobExecutionContext context = new JobExecutionContextImpl();
        context.setJobDetail(expectedScheduleTaskEntity);
        context.setMergedJobDataMap(new JobExecutionContextImpl().getMergedJobDataMap());

        dataArchiveRollbackJob.execute(context);

        // Then
        Assert.assertNotNull(dataArchiveRollbackJob.getJobThread());
        Assert.assertNotNull(dataArchiveRollbackJob.scheduleTaskRepository);
        Assert.assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
    }

    // Test execute method with data archive task not found
    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // Given
        DataArchiveRollbackParameters dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        JobExecutionContext context = new JobExecutionContextImpl();

        // Then
        dataArchiveRollbackJob.execute(context);

        // Assert
        Assert.assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getLastTaskStatus());
    }

    // Test execute method with null context
    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullContext() {
        DataArchiveRollbackParameters dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        dataArchiveRollbackJob.execute(null);
    }

    // Test execute method with null scheduleTaskRepository
    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullScheduleTaskRepository() {
        String jsonString = "{\"dataArchiveTaskId\":1, \"parametersJson\":{\"key\":\"value\"}}";
        ScheduleTaskEntity expectedScheduleTaskEntity = new ScheduleTaskEntity();
        expectedScheduleTaskEntity.setId(1L);
        expectedScheduleTaskEntity.setParametersJson(jsonString);
        expectedScheduleTaskEntity.setJobName("jobName");
        expectedScheduleTaskEntity.setJobGroup("jobGroup");

        DataArchiveRollbackParameters expectedDataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        expectedDataArchiveRollbackParameters.setDataArchiveTaskId(1L);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        dataArchiveRollbackJob.setScheduleTaskRepository(null);

        // When
        JobExecutionContext context = new JobExecutionContextImpl();
        context.setJobDetail(expectedScheduleTaskEntity);
        context.setMergedJobDataMap(new JobExecutionContextImpl().getMergedJobDataMap());

        // Then
        dataArchiveRollbackJob.execute(context);
    }

    // Test getTaskStatus method with null dlmTasks
    @Test
    public void testGetTaskStatusWithNullDlmTasks() {
        // Given
        List<DlmTask> dlmTasks = null;

        // When
        TaskStatus actualTaskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        Assert.assertEquals(TaskStatus.UNKNOWN, actualTaskStatus);
    }

    // Test getTaskStatus method with empty dlmTasks
    @Test
    public void testGetTaskStatusWithEmptyDlmTasks() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();

        // When
        TaskStatus actualTaskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        Assert.assertEquals(TaskStatus.UNKNOWN, actualTaskStatus);
    }

    // Test getTaskStatus method with all tasks done
    @Test
    public void testGetTaskStatusWithAllTasksDone() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask);

        // When
        TaskStatus actualTaskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        Assert.assertEquals(TaskStatus.DONE, actualTaskStatus);
    }

    // Test getTaskStatus method with tasks in progress and done
    @Test
    public void testGetTaskStatusWithTasksInProgressAndDone() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        // When
        TaskStatus actualTaskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        Assert.assertEquals(TaskStatus.PREPARING, actualTaskStatus);
    }
}