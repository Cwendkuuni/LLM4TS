package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> jpaSpecificationExecutor;

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(jpaSpecificationExecutor.findAll(eq(FlowNodeStatus.class), any(ExampleMatcher.class)))
                .thenReturn(expected);

        // When
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(expected, actual);
        verify(jpaSpecificationExecutor, times(1)).findAll(eq(FlowNodeStatus.class), any(ExampleMatcher.class));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1");
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testFindByRoleId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Long roleId = 1L;
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Long userId = 1L;
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        // When
        userTaskInstanceRepository.update(entity);

        // Then
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "userTaskId";

        // When
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setUserTaskId(userTaskId);
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setStatus(status);
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        verify(jpaRepository, times(1)).deleteAll(where(UserTaskInstanceEntity.flowInstanceId.eq(instanceId)));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START,
                "activityId", 1L);

        // Then
        when(jpaRepository.findOne(where(UserTaskInstanceEntity.instanceType.eq(FlowNodeType.START),
                UserTaskInstanceEntity.activityId.eq("activityId"))))
                .thenReturn(entity);
        assertEquals(entity, actual.get());
        verify(jpaRepository, times(1)).findOne(where(UserTaskInstanceEntity.instanceType.eq(FlowNodeType.START),
                UserTaskInstanceEntity.activityId.eq("activityId")));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        // When
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name",
                1L);

        // Then
        when(jpaRepository.findOne(where(UserTaskInstanceEntity.instanceType.eq(FlowNodeType.START),
                UserTaskInstanceEntity.name.eq("name"))))
                .thenReturn(entity);
        assertEquals(entity, actual.get());
        verify(jpaRepository, times(1)).findOne(where(UserTaskInstanceEntity.instanceType.eq(FlowNodeType.START),
                UserTaskInstanceEntity.name.eq("name")));
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        Boolean confirm = true;

        // When
        Boolean actual = userTaskInstanceRepository.findConfirmById(id);

        // Then
        when(jpaRepository.findOne(where(UserTaskInstanceEntity.id.eq(id)).fetchJoin("approval")))
                .thenReturn(new UserTaskInstanceEntity());
        assertEquals(confirm, actual);
        verify(jpaRepository, times(1)).findOne(where(UserTaskInstanceEntity.id.eq(id)).fetchJoin("approval"));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(jpaRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(entity1, entity2));

        // When
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);

        // Then
        List<UserTaskInstanceEntity> expected = Arrays.asList(entity1, entity2);
        assertTrue(actual.containsAll(expected));
        assertEquals(expected.size(), actual.size());
        verify(jpaRepository, times(1)).findAll(any(Specification.class));
    }
}