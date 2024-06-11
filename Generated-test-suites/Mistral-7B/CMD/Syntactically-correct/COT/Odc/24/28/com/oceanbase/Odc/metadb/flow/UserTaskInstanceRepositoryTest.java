package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        // Arrange
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findByStatus(status);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Arrange
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Arrange
        String resourceRoleIdentifier = "roleIdentifier";
        Collection<String> resourceRoleIdentifiers = Arrays.asList("roleIdentifier1", "roleIdentifier2");
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindByRoleId() {
        // Arrange
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findByRoleId(roleId);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        // Arrange
        Long userId = 1L;
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findByCandidateUserId(userId);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testUpdate() {
        // Arrange
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();

        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entityAnswer = invocation.getArgument(0);
            updatedEntity.setId(entityAnswer.getId());
            updatedEntity.setUserTaskId(invocation.getArgument(1).getUserTaskId());
            updatedEntity.setStatus(invocation.getArgument(1).getStatus());
            updatedEntity.setApproved(invocation.getArgument(1).isApproved());
            updatedEntity.setOperatorId(invocation.getArgument(1).getOperatorId());
            updatedEntity.setComment(invocation.getArgument(1).getComment());
            updatedEntity.setExpireIntervalSeconds(invocation.getArgument(1).getExpireIntervalSeconds());
            updatedEntity.setExternalFlowInstanceId(invocation.getArgument(1).getExternalFlowInstanceId());
            return invocation.getArgument(2);
        }).when(userTaskInstanceRepository).update(entity);

        // Act
        int actualRowsAffected = userTaskInstanceRepositoryMock.update(updatedEntity);

        // Assert
        Assertions.assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Arrange
        Long id = 1L;
        String userTaskId = "userTaskId";

        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entityAnswer = invocation.getArgument(0);
            entityAnswer.setUserTaskId(userTaskId);
            return invocation.getArgument(1);
        }).when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        // Act
        int actualRowsAffected = userTaskInstanceRepositoryMock.updateUserTaskIdById(id, userTaskId);

        // Assert
        Assertions.assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        // Arrange
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entityAnswer = invocation.getArgument(0);
            entityAnswer.setStatus(status);
            return invocation.getArgument(1);
        }).when(userTaskInstanceRepository).updateStatusById(id, status);

        // Act
        int actualRowsAffected = userTaskInstanceRepositoryMock.updateStatusById(id, status);

        // Assert
        Assertions.assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Arrange
        Long instanceId = 1L;

        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entityAnswer = invocation.getArgument(0);
            // Perform some business logic or side effects in the real implementation
            return 1;
        }).when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        // Act
        int actualRowsAffected = userTaskInstanceRepositoryMock.deleteByFlowInstanceId(instanceId);

        // Assert
        Assertions.assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Arrange
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activityId";
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, instanceId)).thenReturn(Optional.of(expectedUserTaskInstance));

        // Act
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(instanceType, activityId, instanceId);

        // Assert
        Assertions.assertTrue(actualUserTaskInstance.isPresent());
        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Arrange
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name";
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, instanceId)).thenReturn(Optional.of(expectedUserTaskInstance));

        // Act
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepositoryMock.findByInstanceTypeAndName(instanceType, name, instanceId);

        // Assert
        Assertions.assertTrue(actualUserTaskInstance.isPresent());
        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    @Test
    public void testFindConfirmById() {
        // Arrange
        Long id = 1L;
        Boolean confirm = true;

        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        // Act
        Boolean actualConfirm = userTaskInstanceRepositoryMock.findConfirmById(id);

        // Assert
        Assertions.assertEquals(confirm, actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Arrange
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status)).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(expectedUserTaskInstance));

        // Act
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepositoryMock.findById(id);

        // Assert
        Assertions.assertTrue(actualUserTaskInstance.isPresent());
        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(expectedUserTaskInstanceList);

        // Act
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepositoryMock.findAll();

        // Assert
        Assertions.assertNotNull(actualUserTaskInstanceList);
        assertEquals(expectedUserTaskInstanceList.size(), actualUserTaskInstanceList.size());
    }

    @Test
    public void testFindEmpty() {
        // Act
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepositoryMock.findById(1L);

        // Assert
        assertNull(actualUserTaskInstance.get());
    }

}