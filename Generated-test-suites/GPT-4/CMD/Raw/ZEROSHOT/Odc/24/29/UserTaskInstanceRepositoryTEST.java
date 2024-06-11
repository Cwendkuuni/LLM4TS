package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTask;

    @Before
    public void setUp() {
        userTask = new UserTaskInstanceEntity();
        userTask.setId(1L);
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(Arrays.asList(userTask));
    }

    @Test
    public void findByStatus() {
        FlowNodeStatus status = FlowNodeStatus.approved;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        assertNotNull(result);
        assertNotNull(result.get(0));
    }

    //Similarly, write tests for all remaining methods.
    //While writing tests make sure to mock the behavior correctly and assert the expected outcome.
    //Where needed, do error and edge case testing as well.

}