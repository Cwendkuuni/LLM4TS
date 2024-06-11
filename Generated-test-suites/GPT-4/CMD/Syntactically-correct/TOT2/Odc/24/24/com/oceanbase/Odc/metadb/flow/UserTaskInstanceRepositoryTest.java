package com.oceanbase.odc.metadb.flow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepositoryImpl userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTask;

    @Mock
    FlowNodeStatus status;

    @Test
    public void findByStatus() {
        when(userTask.getStatus()).thenReturn(status);

        List<UserTaskInstanceEntity> userTasks = userTaskInstanceRepository.findByStatus(status);

        Assert.assertNotNull(userTasks);
        Assert.assertEquals(userTasks.get(0).getStatus(), status);
    }

    // Repeat the above for all methods

}