package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);

        flowableAdaptorImpl.setProcessInstanceId(1L, "test");

        verify(flowInstanceRepository, times(1)).findById(anyLong());
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(anyLong(), anyString());
    }

    @Test(expected = RuntimeException.class)
    public void testSetProcessInstanceIdFail() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.empty());

        flowableAdaptorImpl.setProcessInstanceId(1L, "test");

        verify(flowInstanceRepository, times(1)).findById(anyLong());
    }
}
