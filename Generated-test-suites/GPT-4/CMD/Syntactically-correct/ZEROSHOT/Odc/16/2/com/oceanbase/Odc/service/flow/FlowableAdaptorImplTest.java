package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.instance.*;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import java.util.stream.Collectors;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;
    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;
    @Mock
    ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    EventPublisher eventPublisher;
    @Mock
    SequenceInstanceRepository sequenceInstanceRepository;
    @Mock
    FlowInstanceRepository flowInstanceRepository;
    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    TaskService taskService;
    @Mock
    FormService formService;
    @Mock
    AuthenticationFacade authenticationFacade;
    @Mock
    GateWayInstanceRepository gateWayInstanceRepository;

    @Test
    public void setProcessInstanceIdTest() {
        // Prepare input and output
        Long flowInstanceId = 101L;
        String processInstanceId = "test1";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        // Execute the method
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify the correctness
        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceIdTest() {
        // Prepare input and output
        String processInstanceId = "test1";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(101L);
        List<FlowInstanceEntity> entityList = Collections.singletonList(entity);

        when(flowInstanceRepository.findAll(any())).thenReturn(entityList);
        
        // Execute the method
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Verify the correctness
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(entity.getId(), result.get());
    }

    // Continue similar tests for the remaining methods in the FlowableAdaptorImpl class.
    
}
