
package com.oceanbase.odc.service.flow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);
        entity.setProcessInstanceId(processInstanceId);
        
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
}
