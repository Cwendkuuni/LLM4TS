
package com.oceanbase.odc.metadb.flow;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserTaskInstanceRepositoryTest {
    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @InjectMocks
    UserTaskInstanceRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.WAIT_FOR_APPROVAL;
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(new UserTaskInstanceEntity()));
        List<UserTaskInstanceEntity> result = repository.findByStatus(status);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = mock(UserTaskInstanceEntity.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        int result = repository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;   
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(new UserTaskInstanceEntity()));
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(userId,roleIds);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(true);
        Boolean result = repository.findConfirmById(id);
        assertTrue(result);
    }

    //Add more test methods based on this pattern
}
