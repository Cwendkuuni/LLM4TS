package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask underTest;

    @Mock
    TaskService mockedTaskService;

    @Mock
    DelegateExecution mockedExecution;

    @Mock
    User mockedCreator;

    // add all other necessary mocks here...

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startTest() throws Exception{
        // Given
        Long taskId = 1L;
        when(mockedTaskService.start(taskId)).thenReturn(true);
        // need to add more extensive setup here to provide all necessary data...

        // When
        Void result = underTest.start(taskId, mockedTaskService, mockedExecution);

        // Then
        verify(mockedTaskService, times(1)).start(taskId);
        Assert.assertNull(result);
    }

    // add similar test methods for rest of the public methods like isSuccessful(), isFailure(), onFailure(), onSuccessful(), onTimeout(), onProgressUpdate(), cancel() and isCancelled()...

}