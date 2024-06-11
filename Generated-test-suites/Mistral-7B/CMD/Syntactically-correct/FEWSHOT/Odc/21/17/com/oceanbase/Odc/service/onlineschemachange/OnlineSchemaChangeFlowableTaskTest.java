package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.schedule.model.ScheduleEntityTest;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntityTest;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.task.model.TaskEntityTest;

public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleService scheduleService;
    private ScheduleTaskService scheduleTaskService;
    private QuartzJobService quartzJobService;
    private TaskService taskService;
    private ConnectionConfig connectionConfig;
    private OrganizationService organizationService;
    private OscTaskCompleteHandler oscTaskCompleteHandler;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        scheduleTaskService = Mockito.mock(ScheduleTaskService.class);
        quartzJobService = Mockito.mock(QuartzJobService.class);
        taskService = Mockito.mock(TaskService.class);
        organizationService = Mockito.mock(OrganizationService.class);
        oscTaskCompleteHandler = Mockito.mock(OscTaskCompleteHandler.class);
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
        onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
        onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
        onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
        onlineSchemaChangeFlowableTask.setTaskService(taskService);
        onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
        onlineSchemaChangeFlowableTask.setOscTaskCompleteHandler(oscTaskCompleteHandler);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
    }

    @Test
    public void testStart() throws Exception {
        // Given
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        when(execution.getProcessInstanceBusinessKey()).thenReturn(processInstance.getId());
        when(processInstance.getBusinessKey()).thenReturn("businessKey");
        when(execution.getVariables()).thenReturn(Mockito.mock(Map.class));

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        when(execution.getVariable("onlineSchemaChangeParameters")).thenReturn(onlineSchemaChangeParameters);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());

        ConnectionConfig connectionConfigMock = new ConnectionConfig();
        connectionConfigMock.setId("connectionMockId");
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfigMock);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn("schemaName");

        OnlineSchemaChangeContextHolder.setTrace(1L, "taskId", 1L);

        ScheduleEntity scheduleEntity = ScheduleEntityTest.createScheduleEntity();
        when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = List.of(
                new OnlineSchemaChangeScheduleTaskParameters(),
                new OnlineSchemaChangeScheduleTaskParameters()
        );
        when(onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfigMock, "schemaName")).thenReturn(subTaskParameters);

        // When
        onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

        // Then
        verify(taskService, times(1)).start(1L);
        verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), eq(scheduleEntity.getId()));
    }
}

class OnlineSchemaChangeScheduleTaskParametersTest {
    // Test cases for OnlineSchemaChangeScheduleTaskParameters class
}

class OnlineSchemaChangeTaskResultTest {
    // Test cases for OnlineSchemaChangeTaskResult class
}

class OnlineSchemaChangeTaskHandlerTest {
    // Test cases for OnlineSchemaChangeTaskHandler class
}