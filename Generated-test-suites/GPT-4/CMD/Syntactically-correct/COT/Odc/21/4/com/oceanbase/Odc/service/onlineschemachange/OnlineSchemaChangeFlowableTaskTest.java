package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    TaskService taskServicMock;

    @Mock
    ScheduleTaskEntity scheduleTaskEntityMock;

    @Mock
    OnlineSchemaChangeTaskHandler taskHandlerMock;

    @Mock
    ScheduleService scheduleServiceMock;

    @Mock
    ScheduleTaskService scheduleTaskServiceMock;

    @Mock
    DelegateExecution executionMock;

    @Mock
    ConnectionConfig connectionConfigMock;

    @Mock
    OnlineSchemaChangeParameters parameterMock;

    @InjectMocks
    OnlineSchemaChangeFlowableTask oscFlowableTaskTestInstance;

    @Test
    public void startTest(){
        User userMock = new User();
        userMock.setId(5);
        userMock.setOrganizationId(1);

        when(FlowTaskUtil.getTaskCreator(executionMock)).thenReturn(userMock);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(executionMock)).thenReturn(parameterMock);
        when(FlowTaskUtil.getConnectionConfig(executionMock)).thenReturn(connectionConfigMock);
        when(FlowTaskUtil.getSchemaName(executionMock)).thenReturn("testSchemaName");
        when(parameterMock.isContinueOnError()).thenReturn(true);

        oscFlowableTaskTestInstance.start(10L, taskServicMock, executionMock);
        assertEquals(TaskStatus.RUNNING, oscFlowableTaskTestInstance.getStatus());
    }

    // similar tests for isSuccessful, isFailure, onFailure, onSuccessful, onTimeout, cancel and isCancelled methods

    @Test
    public void progressStatusUpdateTest() {
        Page<ScheduleTaskEntity> tasksMock = mock(Page.class);
        when(tasksMock.getSize()).thenReturn(3);

        List<ScheduleTaskEntity> tasksList = new ArrayList<>();
        ScheduleTaskEntity scheduleTaskEntityMock1 = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntityMock1.getStatus()).thenReturn(TaskStatus.DONE);
        tasksList.add(scheduleTaskEntityMock1);

        ScheduleTaskEntity scheduleTaskEntityMock2 = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntityMock2.getStatus()).thenReturn(TaskStatus.FAILED);
        tasksList.add(scheduleTaskEntityMock2);

        ScheduleTaskEntity scheduleTaskEntityMock3 = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntityMock3.getStatus()).thenReturn(TaskStatus.CANCELED);
        tasksList.add(scheduleTaskEntityMock3);

        when(tasksMock.iterator()).thenReturn(tasksList.iterator());

        oscFlowableTaskTestInstance.progressStatusUpdate(tasksMock);
        assertEquals(TaskStatus.CANCELED, oscFlowableTaskTestInstance.getStatus());
    }

    @Test
    public void progressStatusUpdateContinueOnErrorTest() {
        when(parameterMock.isContinueOnError()).thenReturn(true);
        
        Page<ScheduleTaskEntity> tasksMock = mock(Page.class);
        when(tasksMock.getSize()).thenReturn(2);

        List<ScheduleTaskEntity> tasksList = new ArrayList<>();
        ScheduleTaskEntity scheduleTaskEntityMock1 = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntityMock1.getStatus()).thenReturn(TaskStatus.DONE);
        tasksList.add(scheduleTaskEntityMock1);

        ScheduleTaskEntity scheduleTaskEntityMock2 = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntityMock2.getStatus()).thenReturn(TaskStatus.FAILED);
        tasksList.add(scheduleTaskEntityMock2);

        when(tasksMock.iterator()).thenReturn(tasksList.iterator());

        oscFlowableTaskTestInstance.progressStatusUpdate(tasksMock);
        assertEquals(TaskStatus.FAILED, oscFlowableTaskTestInstance.getStatus());
    }

    @Test
    public void singleTaskPercentageTestPreparationStatus() {
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.PREPARING);
        double percentage = oscFlowableTaskTestInstance.singleTaskPercentage(scheduleTaskEntityMock);
        assertEquals(0, percentage, 0.001);
    }

    @Test
    public void singleTaskPercentageTestRunningStatus() {
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(scheduleTaskEntityMock.getProgressPercentage()).thenReturn(50.0);
        double percentage = oscFlowableTaskTestInstance.singleTaskPercentage(scheduleTaskEntityMock);
        assertEquals(0.5, percentage, 0.001);
    }

    @Test
    public void singleTaskPercentageTestOtherStatus() {
        when(scheduleTaskEntityMock.getStatus()).thenReturn(TaskStatus.DONE);
        double percentage = oscFlowableTaskTestInstance.singleTaskPercentage(scheduleTaskEntityMock);
        assertEquals(1, percentage, 0.001);
    }
}
