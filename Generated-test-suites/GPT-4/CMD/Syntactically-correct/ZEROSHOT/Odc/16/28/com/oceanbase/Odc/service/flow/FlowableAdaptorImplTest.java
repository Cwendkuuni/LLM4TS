
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp(){
        flowableAdaptor = new FlowableAdaptorImpl();

        flowableAdaptor.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor.eventPublisher = mock(EventPublisher.class);
        flowableAdaptor.taskService = mock(TaskService.class);
        flowableAdaptor.formService = mock(FormService.class);
        flowableAdaptor.flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.authenticationFacade = mock(AuthenticationFacade.class);
    }

    @Test
    public void testSetProcessInstanceId(){
        //Define the data and behavior for the mock
        long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId))
            .thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowableAdaptor.flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
            .thenReturn(1);

        //Invoke the method to be tested
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        //verify the invocation with the stub
        verify(flowableAdaptor.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId(){
        //Define the data to be given to the function
        String processInstanceId = "processInstanceId";

        //Prepare the flowable instances
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);

        //Define the behavior for the mock
        when(flowableAdaptor.flowInstanceRepository.findAll(any(Specification.class)))
            .thenReturn(Arrays.asList(flowInstanceEntity));

        //Invoke the method to be tested
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        //Assert
        assertTrue(flowInstanceId.isPresent());
        assertEquals(1L, flowInstanceId.get().longValue());
    }

    // Add remaining test cases in similar manner

}
