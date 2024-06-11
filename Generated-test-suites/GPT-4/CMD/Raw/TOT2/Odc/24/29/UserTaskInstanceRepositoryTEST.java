package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskRepo;
    private UserTaskInstanceEntity taskInstance;
  
    @Before
    public void setup() {
        // initial setup code goes here
        taskInstance = new UserTaskInstanceEntity();
    }

    @Test
    public void testfindByStatus() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(taskInstance);
        when(userTaskRepo.findByStatus(any(FlowNodeStatus.class))).thenReturn(list);
        List<UserTaskInstanceEntity> fetchedList = userTaskRepo.findByStatus(FlowNodeStatus.ACTIVE);
        verify(userTaskRepo, times(1)).findByStatus(any(FlowNodeStatus.class));
        assertNotNull(fetchedList);
    }

    // rest of your test cases here. 
    // Follow the same pattern for other methods in UserTaskInstanceRepository interface.
    // e.g., findByCandidateUserIdOrRoleIds, findByRoleId, update, etc. 
}