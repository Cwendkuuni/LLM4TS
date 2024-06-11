package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        entityName.setProcessInstanceId("testProcessId");
        when(flowInstanceRepository.findById(any())).thenReturn(Optional.of(entity));
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "newProcessId");
        assertEquals("newProcessId", flowInstanceRepository.findById(1L).get().getProcessInstanceId());
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(1L, "newProcessDefinitionId");
        assertEquals("newProcessDefinitionId", flowInstanceRepository.findById(1L).get().getProcessDefinitionId());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        assertEquals("testProcessId", result.get());
    }

    @Test
    public void testSetFlowableElement() {
        FlowableElement element = new FlowableElement("activityId", "name", FlowableElementType.USER_TASK);
        flowableAdaptorImpl.setFlowableElement(new BaseFlowNodeInstance(), element);

        verify(nodeInstanceRepository, times(1))
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(any(), any(), any());
    }

    // Repeat the similar approach to test remaining methods
}