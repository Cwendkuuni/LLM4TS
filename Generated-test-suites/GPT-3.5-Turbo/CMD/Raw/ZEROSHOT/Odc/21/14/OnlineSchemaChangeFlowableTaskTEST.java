import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    TaskService taskService;
    @Mock
    DelegateExecution delegateExecution;
    @Mock
    OscTaskCompleteHandler taskHandler;
    @Mock
    ScheduleService scheduleService;
    @Mock
    ScheduleTaskService scheduleTaskService;
    @Mock
    QuartzJobService quartzJobService;
    @Mock
    OrganizationService organizationService;

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.scheduleTaskRepository = scheduleTaskRepository;
        task.taskHandler = taskHandler;
        task.scheduleService = scheduleService;
        task.scheduleTaskService = scheduleTaskService;
        task.quartzJobService = quartzJobService;
        task.organizationService = organizationService;

        Long taskId = 1L;
        User user = new User();
        when(FlowTaskUtil.getTaskCreator(delegateExecution)).thenReturn(user);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(FlowTaskUtil.getConnectionConfig(delegateExecution)).thenReturn(connectionConfig);

        OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(delegateExecution)).thenReturn(parameter);

        String schema = "testSchema";
        when(FlowTaskUtil.getSchemaName(delegateExecution)).thenReturn(schema);

        when(scheduleService.create(Mockito.any(ScheduleEntity.class))).thenReturn(new ScheduleEntity());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        subTaskParameters.add(subTaskParameter);
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(subTaskParameters);

        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity());
        when(scheduleTaskService.listTask(Mockito.any(), Mockito.anyLong())).thenReturn(new PageImpl<>(tasks));

        when(taskService.detail(Mockito.anyLong())).thenReturn(new TaskEntity());

        task.start(taskId, taskService, delegateExecution);

        // Add assertions based on the expected behavior
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.DONE;
        assertEquals(true, task.isSuccessful());

        task.status = TaskStatus.RUNNING;
        assertEquals(false, task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.FAILED;
        assertEquals(true, task.isFailure());

        task.status = TaskStatus.DONE;
        assertEquals(false, task.isFailure());
    }

    @Test
    public void testOnFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        // Add Mockito assertions to verify the method behavior
    }

    @Test
    public void testOnSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        // Add Mockito assertions to verify the method behavior
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        // Add Mockito assertions to verify the method behavior
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        // Add Mockito assertions to verify the method behavior
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;

        // Add Mockito assertions to verify the method behavior
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.CANCELED;
        assertEquals(true, task.isCancelled());

        task.status = TaskStatus.FAILED;
        assertEquals(false, task.isCancelled());
    }

    @Test
    public void testProgressStatusUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        List<ScheduleTaskEntity> tasks = Arrays.asList(new ScheduleTaskEntity());

        // Add assertions based on the expected behavior
    }

    @Test
    public void testSingleTaskPercentage() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        // Add assertions based on the expected behavior
    }
}