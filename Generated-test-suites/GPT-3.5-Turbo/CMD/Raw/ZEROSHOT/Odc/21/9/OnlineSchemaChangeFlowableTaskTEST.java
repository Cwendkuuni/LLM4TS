import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.TaskService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.flowable.engine.delegate.DelegateExecution;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {
  
  private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

  @Mock
  private ScheduleTaskRepository scheduleTaskRepository;
  @Mock
  private ScheduleService scheduleService;
  @Mock
  private ScheduleTaskService scheduleTaskService;
  @Mock
  private TaskService taskService;
  @Mock
  private QuartzJobService quartzJobService;
  @Mock
  private OnlineSchemaChangeTaskHandler taskHandler;
  @Mock
  private OscTaskCompleteHandler completeHandler;
  @Mock
  private OrganizationService organizationService;
  @Mock
  private ConnectionConfig connectionConfig;
  @Mock
  private OnlineSchemaChangeParameters parameter;
  @Mock
  private DelegateExecution execution;
  
  @Before
  public void setUp() {
    onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
    onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
    onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
    onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
    onlineSchemaChangeFlowableTask.setTaskService(taskService);
    onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
    onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
    onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
    onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
    when(parameter.isContinueOnError()).thenReturn(true);
    when(connectionConfig.id()).thenReturn("testId");
    when(connectionConfig.getName()).thenReturn("testName");
    when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
  }
  
  @Test
  public void testStart() throws Exception {
    assertNotNull(onlineSchemaChangeFlowableTask.start(1L, taskService, execution));
  }

  @Test
  public void testIsSuccessful() {
    assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
  }

  @Test
  public void testIsFailure() {
    assertFalse(onlineSchemaChangeFlowableTask.isFailure());
  }

  @Test
  public void testOnFailure() {
    onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
  }

  @Test
  public void testOnSuccessful() {
    onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
  }

  @Test
  public void testOnTimeout() {
    onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
  }

  @Test
  public void testOnProgressUpdate() {
    onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
  }

  @Test
  public void testCancel() {
    assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
  }

  @Test
  public void testIsCancelled() {
    assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
  }

  @Test
  public void testCreateScheduleEntity() {
    assertNotNull(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "testSchema"));
  }

  @Test
  public void testCreateScheduleTaskEntity() {
    assertNotNull(onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, new OnlineSchemaChangeScheduleTaskParameters()));
  }

}