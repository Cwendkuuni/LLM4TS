package com.oceanbase.odc.service.onlineschemachange;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.common.impl.interceptor.TestExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntityImpl;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Deployment
@Rule
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask testSubject;

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
    private TaskService taskService;

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setId(1L);
        parameter = new OnlineSchemaChangeParameters();
        parameter.setId(1L);
        parameter.setGenerateSubTaskParameters(ListUtils.emptyList());
    }

    @Test
    public void testStart() throws Exception {
        // GIVEN
        TaskEntity taskEntity = new TaskEntityImpl();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.APPROVED);
        TestExecutionListener executionListener = new TestExecutionListener();
        ExecutionEntity execution = new ExecutionEntityImpl();
        execution.setId(1L);
        execution.setExecutionListener(executionListener);
        when(taskService.detail(anyLong())).thenReturn(taskEntity);
        when(taskService.start(anyLong())).thenReturn(null);

        // WHEN
        testSubject.start(1L, taskService, execution);

        // THEN
        Assertions.assertNull(testSubject.getStatus());
        Assertions.assertEquals(1L, testSubject.getScheduleId());
        Assertions.assertEquals(1L, testSubject.getCreatorId());
        Assertions.assertEquals(1L, testSubject.getOrganizationId());
        Assertions.assertFalse(testSubject.isContinueOnError());
        Assertions.assertEquals(0, testSubject.getPercentage());
        executionListener.assertListSize(1);
        Assertions.assertEquals(TaskStatus.RUNNING, executionListener.getEventNotifications().get(0).getExecution().getStatus());
    }

    @Test
    public void testProgressUpdate() throws Exception {
        // GIVEN
        Page<ScheduleTaskEntity> tasks = Page.empty();
        when(scheduleTaskService.listTask(any(Pageable.class), eq(testSubject.getScheduleId()))).thenReturn(tasks);

        // WHEN
        testSubject.onProgressUpdate(1L, taskService);

        // THEN
        Assertions.assertEquals(TaskStatus.RUNNING, testSubject.getStatus());
        Assertions.assertEquals(1, tasks.getSize());
        Assertions.assertEquals(TaskStatus.RUNNING, tasks.get(0).getStatus());
        Assertions.assertEquals(0.0, tasks.get(0).getProgressPercentage());
    }

    @Test
    public void testSuccessful() throws Exception {
        // GIVEN
        Page<ScheduleTaskEntity> tasks = createSuccessfulTasks(2);

        // WHEN
        testSubject.onProgressUpdate(1L, taskService);

        // THEN
        Assertions.assertEquals(TaskStatus.DONE, testSubject.getStatus());
        Assertions.assertEquals(2, tasks.getSize());
        Assertions.assertEquals(TaskStatus.DONE, tasks.get(0).getStatus());
        Assertions.assertEquals(TaskStatus.DONE, tasks.get(1).getStatus());
        Assertions.assertNotNull(testSubject.getResultJson());
    }

    @Test
    public void testFailure() throws Exception {
        // GIVEN
        Page<ScheduleTaskEntity> tasks = createFailedTasks(2);

        // WHEN
        testSubject.onProgressUpdate(1L, taskService);

        // THEN
        Assertions.assertEquals(TaskStatus.FAILED, testSubject.getStatus());
        Assertions.assertEquals(2, tasks.getSize());
        Assertions.assertEquals(TaskStatus.FAILED, tasks.get(0).getStatus());
        Assertions.assertEquals(TaskStatus.FAILED, tasks.get(1).getStatus());
        Assertions.assertNotNull(testSubject.getResultJson());
    }

    @Test
    public void testCancel() throws Exception {
        // GIVEN
        Page<ScheduleTaskEntity> tasks = createRunningTasks(2);

        // WHEN
        testSubject.cancel(true, 1L, taskService);

        // THEN
        Assertions.assertEquals(TaskStatus.CANCELED, testSubject.getStatus());
        Assertions.assertEquals(2, tasks.getSize());
        Assertions.assertEquals(TaskStatus.CANCELED, tasks.get(0).getStatus());
        Assertions.assertEquals(TaskStatus.CANCELED, tasks.get(1).getStatus());
    }

    private Page<ScheduleTaskEntity> createRunningTasks(int size) {
        List<ScheduleTaskEntity> tasks = ListUtils.emptyListWith(null, size);
        for (int i = 0; i < size; i++) {
            tasks.set(i, new ScheduleTaskEntity());
            tasks.get(i).setStatus(TaskStatus.RUNNING);
        }
        return new PageImpl<>(tasks);
    }

    private Page<ScheduleTaskEntity> createFailedTasks(int size) {
        List<ScheduleTaskEntity> tasks = ListUtils.emptyListWith(null, size);
        for (int i = 0; i < size; i++) {
            tasks.set(i, new ScheduleTaskEntity());
            tasks.get(i).setStatus(TaskStatus.FAILED);
        }
        return new PageImpl<>(tasks);
    }

    private Page<ScheduleTaskEntity> createSuccessfulTasks(int size) {
        List<ScheduleTaskEntity> tasks = ListUtils.emptyListWith(null, size);
        for (int i = 0; i < size; i++) {
            tasks.set(i, new ScheduleTaskEntity());
            tasks.get(i).setStatus(TaskStatus.DONE);
        }
        return new PageImpl<>(tasks);
    }
}