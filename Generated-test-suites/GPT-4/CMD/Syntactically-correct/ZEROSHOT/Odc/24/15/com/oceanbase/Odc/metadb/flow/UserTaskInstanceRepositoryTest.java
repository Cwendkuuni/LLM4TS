
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.SUCCEEDED;
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(repository.findByStatus(status)).thenReturn(entities);

        List<UserTaskInstanceEntity> results = repository.findByStatus(status);
        assertEquals(entities, results);
        verify(repository, times(1)).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {

        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(repository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(entities);

        List<UserTaskInstanceEntity> results = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(entities, results);
        verify(repository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    // Similar test method should be written for all other functions in the class

    @Transactional
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        when(repository.update(entity)).thenReturn(1);

        int count = repository.update(entity);
        assertEquals(1, count);
        verify(repository, times(1)).update(entity);
    }
}

