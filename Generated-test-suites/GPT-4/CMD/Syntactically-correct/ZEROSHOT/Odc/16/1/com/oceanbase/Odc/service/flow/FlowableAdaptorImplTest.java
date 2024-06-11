
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.authority.util.PreConditions;
import com.oceanbase.odc.core.flow.model.FlowElement;
import com.oceanbase.odc.core.flow.model.FlowElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    // Mock all the other needed components/beans

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId("TestProcessInstanceId");
        flowInstanceEntity.setProcessDefinitionId("TestProcessDefinitionId");

        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), any())).thenReturn(1);
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), any())).thenReturn(1);

        // Set up all the other mocks stubs
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "TestProcessInstanceId");
        verify(flowInstanceRepository, times(1)).findById(1L);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(anyLong(), any());
    }

    // Repeat the similar pattern for all methods in the class

}
