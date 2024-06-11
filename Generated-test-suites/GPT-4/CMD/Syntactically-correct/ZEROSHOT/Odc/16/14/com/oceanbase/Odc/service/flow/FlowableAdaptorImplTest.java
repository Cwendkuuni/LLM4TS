
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;
    
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Before
    public void setup() {
        // Here, we can set up mocks to return some dummy values if need.
    }

    @Test
    public void testSetProcessInstanceId() {
        // Arrange
        Long flowInstanceId = 123L;
        String processInstanceId = "123";
        Optional<FlowInstanceEntity> flowInstanceEntityOptional = Optional.of(new FlowInstanceEntity());
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(flowInstanceEntityOptional);
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        // Act
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        // Assert
        Mockito.verify(flowInstanceRepository).findById(flowInstanceId);
        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Arrange
        String processInstanceId = "123";
        Specification<FlowInstanceEntity> spec = Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        List<FlowInstanceEntity> entityList = new ArrayList<>();
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(123L);
        entityList.add(entity);

        Mockito.when(flowInstanceRepository.findAll(spec)).thenReturn(entityList);

        // Act
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(123), result.get());
        Mockito.verify(flowInstanceRepository).findAll(spec);
    }

    // Continue writing test methods for other methods in the class...
}

