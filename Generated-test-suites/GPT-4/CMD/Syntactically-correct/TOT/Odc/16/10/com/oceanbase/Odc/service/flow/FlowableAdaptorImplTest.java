package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl service;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    // Mock other dependencies...

    @Before
    public void setUp() {
        // Initialize data for the tests
    }

    @Test
    public void test_setProcessInstanceId() {
        // testing setProcessInstanceId() method
    }

    @Test
    public void test_getFlowInstanceIdByProcessInstanceId() {
       // testing getFlowInstanceIdByProcessInstanceId() method
    }

    // Test other methods...

}