package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;

public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImplUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        // Mocking flowInstanceRepository.findById(Long id) method
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(flowInstanceEntity);

        flowableAdaptorImplUnderTest.setProcessInstanceId(0L, "processInstanceId");

        verify(flowInstanceRepository).updateProcessInstanceIdById(0L, "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        //TODO
    }

    @Test
    public void testSetProcessDefinitionId() {
        //TODO
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        //TODO
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        //TODO
    }

    @Test
    public void testGetFlowableElementByType() {
        //TODO
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        //TODO
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        //TODO
    }

    @Test
    public void testGetApprovalInstanceByName() {
        //TODO
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        //TODO
    }

    @Test
    public void testSetFlowableElement() {
        //TODO
    }

    @Test
    public void testInnerConvert() {
        //TODO
    }
}
