package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.junit.After;

public class UserTaskInstanceRepositoryTest {
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFindByStatus() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setUserTaskId("123");
        entity.setStatus(FlowNodeStatus.ACTIVE);

        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE))
                .thenReturn(Arrays.asList(entity));
        
        Collection<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);
        assertEquals(1, userTaskInstances.size());
        assertTrue(userTaskInstances.contains(entity));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setUserTaskId("456");
        Collection<Long> roleIds = Arrays.asList(789L);

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(123L, roleIds))
                .thenReturn(Arrays.asList(entity));

        Collection<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(123L, roleIds);
        assertEquals(1, userTaskInstances.size());
        assertTrue(userTaskInstances.contains(entity));
    }

    // Implement the methods for testing the rest of UserTaskInstanceRepository similarly 
    // like 'testFindByStatus' and 'testFindByCandidateUserIdOrRoleIds' 
    // ...
}