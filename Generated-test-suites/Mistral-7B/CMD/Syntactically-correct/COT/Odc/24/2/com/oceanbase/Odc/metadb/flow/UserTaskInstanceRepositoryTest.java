package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // given
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);

        // then
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // given
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // given
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // given
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }
    }

    @Test
    public void testFindByRoleId() {
        // given
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByRoleId(roleId))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }
    }

    @Test
    public void testFindByCandidateUserId() {
        // given
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }
    }

    @Test
    public void testUpdate() {
        // given
        UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
        Long id = 1L;
        String userTaskId = "userTaskId";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        boolean approved = true;
        Long operatorId = 1L;
        String comment = "comment";
        long expireIntervalSeconds = 1000L;
        String externalFlowInstanceId = "externalFlowInstanceId";

        Mockito.when(entity.getId()).thenReturn(id);
        Mockito.when(entity.getUserTaskId()).thenReturn(null);
        Mockito.when(entity.getStatus()).thenReturn(FlowNodeStatus.CREATED);
        Mockito.when(entity.isApproved()).thenReturn(false);
        Mockito.when(entity.getOperatorId()).thenReturn(null);
        Mockito.when(entity.getComment()).thenReturn(null);
        Mockito.when(entity.getExpireIntervalSeconds()).thenReturn(0L);
        Mockito.when(entity.getExternalFlowInstanceId()).thenReturn(null);

        int updatedRows = userTaskInstanceRepository.update(entity);

        Mockito.verify(entity).setUserTaskId(userTaskId);
        Mockito.verify(entity).setStatus(status);
        Mockito.verify(entity).setApproved(approved);
        Mockito.verify(entity).setOperatorId(operatorId);
        Mockito.verify(entity).setComment(comment);
        Mockito.verify(entity).setExpireIntervalSeconds(expireIntervalSeconds);
        Mockito.verify(entity).setExternalFlowInstanceId(externalFlowInstanceId);

        assertEquals(1, updatedRows);

        // when
        updatedRows = userTaskInstanceRepository.update(entity);

        // then
        Mockito.verify(entity, Mockito.times(1)).setUserTaskId(userTaskId);
        Mockito.verify(entity, Mockito.times(1)).setStatus(status);
        Mockito.verify(entity, Mockito.times(1)).setApproved(approved);
        Mockito.verify(entity, Mockito.times(1)).setOperatorId(operatorId);
        Mockito.verify(entity, Mockito.times(1)).setComment(comment);
        Mockito.verify(entity, Mockito.times(1)).setExpireIntervalSeconds(expireIntervalSeconds);
        Mockito.verify(entity, Mockito.times(1)).setExternalFlowInstanceId(externalFlowInstanceId);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // given
        Long id = 1L;
        String userTaskId = "userTaskId";

        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        Mockito.verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
        assertEquals(1, updatedRows);

        // given
        id = 2L;
        userTaskId = null;

        updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        Mockito.verifyNoInteractions(userTaskInstanceRepository);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        // given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);

        Mockito.verify(userTaskInstanceRepository).updateStatusById(id, status);
        assertEquals(1, updatedRows);

        // given
        id = 2L;
        status = null;

        updatedRows = userTaskInstanceRepository.updateStatusById(id, status);

        Mockito.verifyNoInteractions(userTaskInstanceRepository);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // given
        Long instanceId = 1L;
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        Mockito.verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
        assertEquals(1, deletedRows);

        // given
        instanceId = 2L;

        deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        Mockito.verifyNoInteractions(userTaskInstanceRepository);
        assertEquals(0, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // given
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = Mockito.mock(UserTaskInstanceEntity.class);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(expected));

        // when
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

        // given
        instanceType = null;

        // when
        Optional<UserTaskInstanceEntity> actual2 = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // then
        assertFalse(actual2.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // given
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = Mockito.mock(UserTaskInstanceEntity.class);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(expected));

        // when
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());

        // given
        name = null;

        // when
        Optional<UserTaskInstanceEntity> actual2 = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        // then
        assertFalse(actual2.isPresent());

        // given
        flowInstanceId = null;

        // when
        Optional<UserTaskInstanceEntity> actual3 = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        // then
        assertFalse(actual3.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        // given
        Long id = 1L;
        boolean confirm = true;

        boolean actual = userTaskInstanceRepository.findConfirmById(id);

        Mockito.verify(userTaskInstanceRepository).findConfirmById(id);
        assertEquals(confirm, actual);

        // given
        confirm = false;

        actual = userTaskInstanceRepository.findConfirmById(id);

        Mockito.verifyNoInteractions(userTaskInstanceRepository);
        assertEquals(false, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // given
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);

        // then
        assertNotNull(actual);
        assertEquals(2, actual.size());
        for (UserTaskInstanceEntity entity : actual) {
            assertNotNull(entity);
        }

        // given
        status = null;

        // when
        List<UserTaskInstanceEntity> actual2 = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);

        // then
        assertNull(actual2);
    }
}