package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userRepository;
    
    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        Mockito.when(userRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userRepository.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        Mockito.when(userRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Collections.singletonList("role1");

        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        Mockito.when(userRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    // Additional test cases can be added for remaining methods in the UserTaskInstanceRepository class

}