package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {
   
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @Mock
    private FlowableElement flowableElement;
   
    @Mock
    private FlowInstanceEntity flowInstanceEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetFlowableElement() {
        FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();

        when(flowableElement.getType()).thenReturn(FlowableElementType.SERVICE_TASK);

        flowableAdaptorImpl.setFlowableElement(
                new BaseFlowNodeInstance(),
                flowableElement
        );

        // you will have to write assert commands here to test this method

    }

   @Test
    public void testSetProcessInstanceId() {
        long flowInstanceId = 123;
        String processInstanceId = "test";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        int affectedRows = flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(1, affectedRows);
    }

   @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "test";
        long instanceId = 123;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<Long> flowInstanceId = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(Optional.of(instanceId), flowInstanceId);
    }

    // and so on for each method ..

}