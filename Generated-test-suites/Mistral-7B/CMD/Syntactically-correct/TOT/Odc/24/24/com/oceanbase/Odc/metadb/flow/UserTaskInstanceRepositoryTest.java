package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private JpaEntityInformation<UserTaskInstanceEntity, Long> userTaskInstanceEntityInformation;

    @Before
    public void setup() {
        userTaskInstanceRepository = Mockito.spy(UserTaskInstanceRepositoryImpl.class);
        userTaskInstanceEntityInformation = Mockito.spy(UserTaskInstanceEntityInformationImpl.class);
        Mockito.doReturn(userTaskInstanceEntityInformation).when(userTaskInstanceRepository)
                .getEntityInformation();
    }

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.doReturn(expectedUserTaskInstances).when(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        Mockito.doReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2))
                .when(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(userId, roleIds);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertEquals(2, actualUserTaskInstances.size());
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity1));
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity2));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        Mockito.doReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2))
                .when(userTaskInstanceRepository).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        // Then
        assertEquals(2, actualUserTaskInstances.size());
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity1));
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity2));
    }

    @Test
    public void testFindByRoleId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Long roleId = 1L;

        Mockito.doReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2))
                .when(userTaskInstanceRepository).findByRoleId(roleId);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);

        // Then
        assertEquals(2, actualUserTaskInstances.size());
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity1));
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity2));
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Long userId = 1L;

        Mockito.doReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2))
                .when(userTaskInstanceRepository).findByCandidateUserId(userId);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);

        // Then
        assertEquals(2, actualUserTaskInstances.size());
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity1));
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity2));
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        Long id = 1L;
        String userTaskId = "userTaskId";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Boolean approved = true;
        Long operatorId = 1L;
        String comment = "comment";
        Long expireIntervalSeconds = 1L;
        String externalFlowInstanceId = "externalFlowInstanceId";

        Mockito.doNothing().when(userTaskInstanceRepository).update(userTaskInstanceEntity);

        // When
        int actualRowsAffected = userTaskInstanceRepository.update(userTaskInstanceEntity);

        // Then
        assertEquals(1, actualRowsAffected);

        // Given (update with different values)
        UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity();
        Mockito.doReturn(userTaskInstanceEntity).when(userTaskInstanceRepository).findById(id);
        Mockito.doNothing().when(userTaskInstanceRepository).update(updatedUserTaskInstanceEntity);

        // When
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setUserTaskId(userTaskId);
        entity.setStatus(status);
        entity.setApproved(approved);
        entity.setOperatorId(operatorId);
        entity.setComment(comment);
        entity.setExpireIntervalSeconds(expireIntervalSeconds);
        entity.setExternalFlowInstanceId(externalFlowInstanceId);

        int actualRowsAffected2 = userTaskInstanceRepository.update(entity);

        // Then
        assertEquals(1, actualRowsAffected2);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "userTaskId";

        Mockito.doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        // When
        int actualRowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        Mockito.doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        // When
        int actualRowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        int expectedRowsAffected = 2;

        Mockito.doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        // When
        int actualRowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedRowsAffected, actualRowsAffected);

        // Given (delete with different flowInstanceId)
        Long otherInstanceId = 2L;
        Mockito.doReturn(Collections.singletonList(new UserTaskInstanceEntity())).when(userTaskInstanceRepository)
                .findByFlowInstanceId(otherInstanceId);

        // When
        int actualRowsAffected2 = userTaskInstanceRepository.deleteByFlowInstanceId(otherInstanceId);

        // Then
        assertEquals(1, actualRowsAffected2);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Mockito.doReturn(Optional.of(userTaskInstanceEntity)).when(userTaskInstanceRepository)
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        // When
        Optional<UserTaskInstanceEntity> actualOptionalUserTaskInstanceEntity = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        // Then
        assertTrue(actualOptionalUserTaskInstanceEntity.isPresent());
        assertEquals(userTaskInstanceEntity, actualOptionalUserTaskInstanceEntity.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        Mockito.doReturn(Optional.of(userTaskInstanceEntity)).when(userTaskInstanceRepository)
                .findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        // When
        Optional<UserTaskInstanceEntity> actualOptionalUserTaskInstanceEntity = userTaskInstanceRepository
                .findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        // Then
        assertTrue(actualOptionalUserTaskInstanceEntity.isPresent());
        assertEquals(userTaskInstanceEntity, actualOptionalUserTaskInstanceEntity.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        Boolean confirm = true;

        Mockito.doReturn(confirm).when(userTaskInstanceRepository).findConfirmById(id);

        // When
        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertEquals(confirm, actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        Mockito.doReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2))
                .when(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        // Then
        assertEquals(2, actualUserTaskInstances.size());
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity1));
        assertTrue(actualUserTaskInstances.contains(userTaskInstanceEntity2));
    }

    @Test
    public void testFindAll() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserTaskInstanceEntity> expectedPage = new PageImpl<>(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2), pageable, 2);

        Mockito.doReturn(expectedPage).when(userTaskInstanceRepository).findAll(pageable);

        // When
        Page<UserTaskInstanceEntity> actualPage = userTaskInstanceRepository.findAll(pageable);

        // Then
        assertEquals(expectedPage.getContent(), actualPage.getContent());
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedPage.getNumber(), actualPage.getNumber());
        assertEquals(expectedPage.isFirst(), actualPage.isFirst());
        assertEquals(expectedPage.isLast(), actualPage.isLast());
    }
}