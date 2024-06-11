
package com.oceanbase.odc.service.flow;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    private FlowableAdaptorImpl underTest;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowInstanceEntity dummyFlowInstance;

    @Test
    public void testSetProcessInstanceId() {
       // Given
       when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(dummyFlowInstance));
       when(flowInstanceRepository.updateProcessInstanceIdById(1L, "123")).thenReturn(1);

       // When
       underTest.setProcessInstanceId(1L, "123");

       // Then
       verify(flowInstanceRepository).findById(1L);
       verify(flowInstanceRepository).updateProcessInstanceIdById(1L, "123");
    }

   // more test cases for other methods come here
}

