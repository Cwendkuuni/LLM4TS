package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(1L))
            .thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(1L, "testProcessId")).thenReturn(1);
        flowableAdaptorImpl.setProcessInstanceId(1L, "testProcessId");
        verify(flowInstanceRepository).updateProcessInstanceIdById(1L, "testProcessId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        List<FlowInstanceEntity> entities = Arrays.asList(entity);
        when(flowInstanceRepository.findAll(null)).thenReturn(entities);
        Optional<Long> optional = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("test");
        assertEquals(Long.valueOf(1), optional.get());
    }

    // ... More test methods for all methods in the class ...

}