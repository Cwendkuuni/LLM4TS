package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        Long statusId = FlowNodeStatus.APPROVED.getId();
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.APPROVED)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.eq(userId),
                Mockito.eq(roleIds)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("resourceRole1", "resourceRole2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.eq(userId),
                Mockito.eq(resourceRoleIdentifiers)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        Collection<String> resourceRoleIdentifier = Arrays.asList("resourceRole1", "resourceRole2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.eq(resourceRoleIdentifier)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByRoleId() {
        // Given
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByRoleId(Mockito.eq(roleId)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.eq(userId)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
        Long id = 1L;
        Long userTaskId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String comment = "Test comment";
        Long expireIntervalSeconds = 10L;
        Long operatorId = 1L;
        String externalFlowInstanceId = "TestExternalFlowInstanceId";

        Mockito.doNothing().when(userTaskInstanceRepository).update(entity);

        // When
        int actual = userTaskInstanceRepository.update(entity);

        // Then
        fail("Method update should not be invoked when given entity is mocked");
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "TestUserTaskId";

        Mockito.doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        // When
        int actual = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        fail("Method updateUserTaskIdById should not be invoked when given id is mocked");
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        Mockito.doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        // When
        int actual = userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        fail("Method updateStatusById should not be invoked when given id is mocked");
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        int expected = 2;

        Mockito.doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        // When
        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "TestActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = Mockito.mock(UserTaskInstanceEntity.class);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.eq(instanceType),
                Mockito.eq(activityId), Mockito.eq(flowInstanceId)))
                .thenReturn(Optional.of(expected));

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "TestName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = Mockito.mock(UserTaskInstanceEntity.class);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.eq(instanceType),
                Mockito.eq(name), Mockito.eq(flowInstanceId)))
                .thenReturn(Optional.of(expected));

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        // Then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        Boolean expected = true;

        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(expected);

        // When
        Boolean actual = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));

        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.eq(flowInstanceIds),
                Mockito.eq(status)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                status);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testFindAll() {
        // Given
        List<UserTaskInstanceEntity> expected = Arrays.asList(Mockito.mock(UserTaskInstanceEntity.class),
                Mockito.mock(UserTaskInstanceEntity.class));
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<UserTaskInstanceEntity> example = Example.of(new UserTaskInstanceEntity(), matcher);

        Mockito.when(userTaskInstanceRepository.findAll(example)).thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll(example);

        // Then
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}