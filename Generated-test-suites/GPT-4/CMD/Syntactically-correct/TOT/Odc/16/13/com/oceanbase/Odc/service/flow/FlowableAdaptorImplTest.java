package com.oceanbase.odc.service.flow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @InjectMocks 
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
        
    // additional mock objects for all private properties here
    
    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        // test code here
    }

    // additional test methods here 

}