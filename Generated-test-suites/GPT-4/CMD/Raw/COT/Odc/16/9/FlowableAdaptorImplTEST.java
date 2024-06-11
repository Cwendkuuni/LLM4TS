
package com.oceanbase.odc.service.flow;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

import java.util.Optional;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.core.shared.ResourceType;
import org.mockito.InjectMocks;
import org.mockito.Mock;

// other dependencies

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    // other dependencies

    @Before
    public void setUp() throws Exception {
        // Set up your mocks here
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        
        doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
        
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
}
