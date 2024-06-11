package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @InjectMocks
    UserTaskService userTaskService;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        userTaskService.findByStatus(status);
        verify(userTaskInstanceRepositoryMock, times(1)).findByStatus(status);
    }

    // similarly add tests for other methods...

}