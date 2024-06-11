package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.empty());
        flowableAdaptorImpl.setProcessInstanceId(1L, "ABC");
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(anyLong(), anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("ABC");
        assertEquals(Optional.empty(), result);
    }

    //Similar test methods for each of the methods in the service should go here

}