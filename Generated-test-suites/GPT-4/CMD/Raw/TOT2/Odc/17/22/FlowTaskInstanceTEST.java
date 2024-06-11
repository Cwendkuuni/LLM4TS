package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest{
    
    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        flowTaskInstance = new FlowTaskInstance();
    }

    @Test
    public void testUpdate() {
        // Assign
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);

        // Act
        flowTaskInstance.update();

        // Assert
        verify(serviceTaskRepository, only()).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_WithException() {
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenThrow(IllegalArgumentException.class);

        flowTaskInstance.update();
    }
    // more tests
}