
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

  @Mock
  private ScheduleTaskRepository scheduleTaskRepository;
  @Mock
  private ScheduleService scheduleService;
  @Mock
  private ScheduleTaskService scheduleTaskService;
  @Mock
  private QuartzJobService quartzJobService;
  @Mock
  private OnlineSchemaChangeTaskHandler taskHandler;
  @Mock
  private OscTaskCompleteHandler completeHandler;
  @Mock
  private OrganizationService organizationService;
  @Mock
  private FlowTaskUtil flowTaskUtil;

  @InjectMocks
  private OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testStart() throws Exception {
    Long taskId = 1L;
    DelegateExecution execution = mock(DelegateExecution.class);
    TaskService taskService = mock(TaskService.class);
    User user = new User();
    user.setId(1L);
    user.setOrganizationId(1L);
    when(flowTaskUtil.getTaskCreator(execution)).thenReturn(user);
    when(taskService.start(taskId)).thenReturn(true);
    assertNull(task.start(taskId, taskService, execution));
  }

  @Test
  public void testIsSuccessful() {
    task.status = TaskStatus.DONE;
    assertTrue(task.isSuccessful());
    task.status = TaskStatus.RUNNING;
    assertFalse(task.isSuccessful());
  }

  @Test
  public void testIsFailure() {
    task.status = TaskStatus.FAILED;
    assertTrue(task.isFailure());
    task.status = TaskStatus.RUNNING;
    assertFalse(task.isFailure());
  }

  @Test
  public void testIsCancelled() {
    task.status = TaskStatus.CANCELED;
    assertTrue(task.isCancelled());
    task.status = TaskStatus.RUNNING;
    assertFalse(task.isCancelled());
  }

  @Test
  public void testOnFailure() {
    TaskService taskService = mock(TaskService.class);
    Long taskId = 1L;
    task.onFailure(taskId, taskService);

    verify(taskService, times(1)).fail(taskId);
  }

  @Test
  public void testOnSuccessful() {
    TaskService taskService = mock(TaskService.class);
    Long taskId = 1L;
    task.onSuccessful(taskId, taskService);

    verify(taskService, times(1)).finish(taskId);
  }

  @Test
  public void testOnTimeout() {
    TaskService taskService = mock(TaskService.class);
    Long taskId = 1L;
    task.onTimeout(taskId, taskService);

    verify(taskService, times(1)).timeout(taskId);
  }

  @Test
  public void testOnProgressUpdate() {
    TaskService taskService = mock(TaskService.class);
    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
    scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
    Long taskId = 1L;
    task.scheduleId = taskId;
    List<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
    scheduleTaskEntities.add(scheduleTaskEntity);
    Page<ScheduleTaskEntity> entityPage = new PageImpl<>(scheduleTaskEntities);
    when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(entityPage);
    task.onProgressUpdate(taskId, taskService);
  }

  @Test
  public void testCancel() {
    TaskService taskService = mock(TaskService.class);
    Long taskId = 1L;
    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
    scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
    scheduleTaskEntity.setId(1L);
    task.scheduleId = taskId;
    List<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
    scheduleTaskEntities.add(scheduleTaskEntity);
    Page<ScheduleTaskEntity> entityPage = new PageImpl<>(scheduleTaskEntities);
    when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(entityPage);
    assertTrue(task.cancel(true, taskId, taskService));
  }
}
