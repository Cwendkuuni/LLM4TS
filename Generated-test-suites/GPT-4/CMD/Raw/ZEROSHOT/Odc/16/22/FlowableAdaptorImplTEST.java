
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private FormService formService;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(1L, "testProcessId")).thenReturn(1);
        flowableAdaptorImpl.setProcessInstanceId(1L, "testProcessId");
        verify(flowInstanceRepository).updateProcessInstanceIdById(1L, "testProcessId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        List<FlowInstanceEntity> entities = new ArrayList<>();
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        entities.add(entity);
        when(flowInstanceRepository.findAll(null)).thenReturn(entities);
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("testProcessId");
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1), result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(1L, "testProcessId")).thenReturn(1);
        flowableAdaptorImpl.setProcessDefinitionId(1L, "testProcessId");
        verify(flowInstanceRepository).updateProcessDefinitionIdById(1L, "testProcessId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        List<FlowInstanceEntity> entities = new ArrayList<>();
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        entities.add(entity);
        when(flowInstanceRepository.findAll(null)).thenReturn(entities);
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId");
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId("testProcessId");
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        assertTrue(result.isPresent());
        assertEquals("testProcessId", result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "testActivityId", 1L))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId("testActivityId", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, "testActivityId", 1L))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId("testActivityId", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "testName", 1L))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName("testName", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "testActivityId", 1L))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId("testActivityId", 1L);
        assertTrue(result.isPresent());
    }
}

