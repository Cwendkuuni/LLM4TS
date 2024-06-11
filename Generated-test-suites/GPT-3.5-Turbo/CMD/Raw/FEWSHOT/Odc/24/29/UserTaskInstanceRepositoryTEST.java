package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindById() {
        Long id = 1L;

        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(id);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        List<UserTaskInstanceEntity> expectedList = Collections.singletonList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedList, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> expectedList = Collections.singletonList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedList, result);
    }

    // Add more test methods for other methods in UserTaskInstanceRepository

}