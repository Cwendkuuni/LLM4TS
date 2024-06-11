package com.oceanbase.odc.metadb.flow.test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

/**
 * Test class for UserTaskInstanceRepository
 */
@ExtendWith(MockitoExtension.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    private UserTaskInstanceEntity validEntity;

    @BeforeEach
    public void setup() {
        validEntity = new UserTaskInstanceEntityBuilder().build();
    }

    @DisplayName("Test findByStatus method with valid status")
    @Test
    public void testFindByStatus() {
        // Given
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(validEntity));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findByStatus method with empty list")
    @Test
    public void testFindByStatus_emptyList() {
        // Given
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED)).thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.REJECTED);

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findByCandidateUserIdOrRoleIds method with valid parameters")
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> entities = Arrays.asList(validEntity);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findByCandidateUserIdOrRoleIds method with empty list")
    @Test
    public void testFindByCandidateUserIdOrRoleIds_emptyList() {
        // Given
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findByResourceRoleIdentifier method with valid role identifier")
    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        String resourceRoleIdentifier = "admin";
        List<UserTaskInstanceEntity> entities = Arrays.asList(validEntity);
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier)))
                .thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByResourceRoleIdentifier("admin");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findByResourceRoleIdentifier method with empty list")
    @Test
    public void testFindByResourceRoleIdentifier_emptyList() {
        // Given
        List<String> resourceRoleIdentifier = Arrays.asList("admin", "user");
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier)).thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByResourceRoleIdentifier("admin");

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findByRoleId method with valid role id")
    @Test
    public void testFindByRoleId() {
        // Given
        Long roleId = 1L;
        List<UserTaskInstanceEntity> entities = Arrays.asList(validEntity);
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByRoleId(roleId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findByRoleId method with empty list")
    @Test
    public void testFindByRoleId_emptyList() {
        // Given
        Long roleId = 1L;
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByRoleId(roleId);

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findByCandidateUserId method with valid user id")
    @Test
    public void testFindByCandidateUserId() {
        // Given
        Long userId = 1L;
        List<UserTaskInstanceEntity> entities = Arrays.asList(validEntity);
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserId(userId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findByCandidateUserId method with empty list")
    @Test
    public void testFindByCandidateUserId_emptyList() {
        // Given
        Long userId = 1L;
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserId(userId);

        // Then
        assertThat(result).isEmpty();
    }

    @DisplayName("Test update method with valid parameters")
    @Test
    public void testUpdate() {
        // Given
        Long id = 1L;
        UserTaskInstanceEntity entity = validEntity;
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntityBuilder().build();

        // When
        int result = userTaskInstanceService.update(updatedEntity);

        // Then
        verify(userTaskInstanceRepository, times(1)).update(entity);
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("Test updateUserTaskIdById method with valid id and userTaskId")
    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "taskId";

        // When
        int result = userTaskInstanceService.updateUserTaskIdById(id, userTaskId);

        // Then
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(id, userTaskId);
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("Test updateStatusById method with valid id and status")
    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        int result = userTaskInstanceService.updateStatusById(id, status);

        // Then
        verify(userTaskInstanceRepository, times(1)).updateStatusById(id, status);
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("Test deleteByFlowInstanceId method with valid flowInstanceId")
    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;

        // When
        int result = userTaskInstanceService.deleteByFlowInstanceId(flowInstanceId);

        // Then
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(flowInstanceId);
        assertThat(result).isGreaterThan(0);
    }

    @DisplayName("Test findByInstanceTypeAndActivityId method with valid parameters")
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activityId";
        UserTaskInstanceEntity entity = validEntity;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // Then
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(instanceType, activityId,
                flowInstanceId);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(entity);
    }

    @DisplayName("Test findByInstanceTypeAndActivityId method with empty optional")
    @Test
    public void testFindByInstanceTypeAndActivityId_emptyOptional() {
        // Given
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activityId";
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.empty());

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // Then
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(instanceType, activityId,
                flowInstanceId);
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid parameters")
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> entities = Arrays.asList(validEntity);
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED)).thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED);

        // Then
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(validEntity);
    }

    @DisplayName("Test findApprovalInstanceIdByFlowInstanceIdAndStatus method with empty list")
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_emptyList() {
        // Given
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> entities = Collections.emptyList();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED)).thenReturn(entities);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED);

        // Then
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId,
                FlowNodeStatus.APPROVED);
        assertThat(result).isEmpty();
    }

    @DisplayName("Test findConfirmById method with valid id")
    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        // When
        Boolean result = userTaskInstanceService.findConfirmById(id);

        // Then
        verify(userTaskInstanceRepository, times(1)).findConfirmById(id);
        assertThat(result).isEqualTo(confirm);
    }

    @DisplayName("Test findConfirmById method with null confirm")
    @Test
    public void testFindConfirmById_nullConfirm() {
        // Given
        Long id = 1L;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(null);

        // When
        Boolean result = userTaskInstanceService.findConfirmById(id);

        // Then
        verify(userTaskInstanceRepository, times(1)).findConfirmById(id);
        assertThat(result).isNull();
    }
}