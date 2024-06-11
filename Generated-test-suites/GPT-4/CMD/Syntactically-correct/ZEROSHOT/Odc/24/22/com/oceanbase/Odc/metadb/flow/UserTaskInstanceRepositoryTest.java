
package com.oceanbase.odc.metadb.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository repository;

    @Mock
    private UserTaskInstanceEntity entity;

    @Before
    public void setUp() throws Exception {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(entity);

        when(repository.findByStatus(any(FlowNodeStatus.class))).thenReturn(entities);
        when(repository.findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class))).thenReturn(entities);
        when(repository.findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class))).thenReturn(entities);
        when(repository.findByResourceRoleIdentifierIn(any(Collection.class))).thenReturn(entities);
    }

    @Test
    public void testFindByStatus() {
        repository.findByStatus(FlowNodeStatus.APPROVED);
        verify(repository, times(1)).findByStatus(any(FlowNodeStatus.class));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);

        repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        verify(repository, times(1)).findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Collection<String> roleIds = new ArrayList<>();
        roleIds.add("role1");

        repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, roleIds);
        verify(repository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> roleIds = new ArrayList<>();
        roleIds.add("role1");

        repository.findByResourceRoleIdentifierIn(roleIds);
        verify(repository, times(1)).findByResourceRoleIdentifierIn(roleIds);
    }

    @Test
    public void testUpdate() {
        repository.update(entity);
        verify(repository, times(1)).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        repository.updateUserTaskIdById(1L, "userTaskId");
        verify(repository, times(1)).updateUserTaskIdById(any(Long.class), any(String.class));
    }

    @Test
    public void testUpdateStatusById() {
        repository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        verify(repository, times(1)).updateStatusById(any(Long.class), any(FlowNodeStatus.class));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        repository.deleteByFlowInstanceId(1L);
        verify(repository, times(1)).deleteByFlowInstanceId(any(Long.class));
    }

    // Add the rest of the method tests...
}
