import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult.SubTaskResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
 private QuartzJobService quartzJobService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleService scheduleService;
 private ScheduleTaskService scheduleTaskService;
 private OrganizationService organizationService;
 private TaskService taskService;

 @Before
 public void setup() {
 OnlineSchemaChangeContextHolder.clear();
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 quartzJobService = mock(QuartzJobService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleService = mock(ScheduleService.class);
 scheduleTaskService = mock(ScheduleTaskService.class);
 organizationService = mock(OrganizationService.class);
 taskService = mock(TaskService.class);
 when(quartzJobService.terminate(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
 when(scheduleTaskRepository.saveAndFlush(Mockito.any(ScheduleTaskEntity.class))).thenReturn(Mockito.mock(ScheduleTaskEntity.class));
 }

 @Test
 public void testStart_success() throws Exception {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setUid("uid");
 String schema = "schema";
 parameter.generateSubTaskParameters(connectionConfig, schema).forEach(p -> p.setUid(parameter.getUid()));
 Page<ScheduleTaskEntity> tasks = ListUtils.emptyIfNull(parameter.generateSubTaskParameters(connectionConfig, schema)
 .stream().map(param -> createScheduleTaskEntity(param))).map(task -> taskService.detail(task.getId()))
 .collect(Collectors.toList());

 OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
 ScheduleEntity schedule = createScheduleEntity(connectionConfig, parameter, schema);
 when(scheduleService.create(schedule)).thenReturn(schedule);
 when(scheduleTaskService.listTask(Pageable.unpaged(), schedule.getId())).thenReturn(tasks);
 when(taskService.start(1L)).thenReturn(null);

 // when
 onlineSchemaChangeFlowableTask.start(1L, taskService, null);

 // then
 OnlineSchemaChangeTaskResult result = JsonUtils.parse(JsonUtils.toJson(onlineSchemaChangeFlowableTask.getResultJson()),
 OnlineSchemaChangeTaskResult.class);
 assertEquals(TaskStatus.DONE, result.getStatus());
 assertEquals(1, result.getSubTasks().size());
 assertEquals(TaskStatus.DONE, result.getSubTasks().get(0).getStatus());
 }

 @Test
 public void testStart_failure() throws Exception {
 // given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setUid("uid");
 String schema = "schema";
 when(quartzJobService.terminate(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);

 // when & then
 onlineSchemaChangeFlowableTask.start(1L, taskService, null);
 assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
 }

 // ... (add more test cases for edge cases and error scenarios)

 private ScheduleEntity createScheduleEntity(ConnectionConfig connectionConfig,
 OnlineSchemaChangeParameters parameter, String schema) {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 // set properties
 return scheduleEntity;
 }

 private ScheduleTaskEntity createScheduleTaskEntity(OnlineSchemaChangeScheduleTaskParameters param) {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 // set properties
 return scheduleTaskEntity;
 }
}