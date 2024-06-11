package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceServiceImpl service;

    @Mock
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        // Given
        FlowNodeStatus status = FlowNodeStatus.RUNNING;

        // When
        service.findByStatus(status);

        // Then
        verify(repository, times(1)).findByStatus(status);
    }
    
    //Similar tests can be written for all other methods as well
}