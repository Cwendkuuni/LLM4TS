package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

/**
 * Test cases for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-13 11:51
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private SimpleJpaRepository repositoryMock;

    @Before
    public void setUp() {
        repositoryMock = mock(SimpleJpaRepository.class);
        userTaskInstanceRepository = new UserTaskInstanceRepository() {
            {
                setRepository(repositoryMock);
            }
        };
    }

    @After
    public void tearDown() {
        repositoryMock = null;
        userTaskInstanceRepository = null;
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);
        when(repositoryMock.findByStatus(FlowNodeStatus.APPROVED)).thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);

        when(repositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("testRoleIdentifier");

        when(repositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        Collection<String> resourceRoleIdentifier = new ArrayList<>();
        resourceRoleIdentifier.add("testRoleIdentifier");

        when(repositoryMock.findByResourceRoleIdentifierIn(resourceRoleIdentifier)).thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        Long roleId = 1L;

        when(repositoryMock.findByRoleId(roleId)).thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;

        when(repositoryMock.findByCandidateUserId(userId)).thenCallRealMethod();

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.builder().build();
        Long id = 1L;

        when(repositoryMock.findById(id)).thenReturn(Optional.of(entity));

        UserTaskInstanceEntity updatedEntity = UserTaskInstanceEntityBuilder.builder()
                .withId(id)
                .withUserTaskId("testUserTaskId")
                .withStatus(FlowNodeStatus.APPROVED)
                .withApproved(true)
                .withOperatorId(1L)
                .withComment("testComment")
                .withExpireIntervalSeconds(1000)
                .withExternalFlowInstanceId("testExternalFlowInstanceId")
                .build();

        when(repositoryMock.save(entity)).thenReturn(updatedEntity);

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);

        assertEquals("testUserTaskId", entity.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, entity.getStatus());
        assertEquals(true, entity.isApproved());
        assertEquals(1L, entity.getOperatorId());
        assertEquals("testComment", entity.getComment());
        assertEquals(1000, entity.getExpireIntervalSeconds());
        assertEquals("testExternalFlowInstanceId", entity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);

        assertEquals(FlowNodeStatus.APPROVED,
                userTaskInstanceRepository.findById(id).get().getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long id = 1L;
        FlowNodeType instanceType = FlowNodeType.TEST;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntityBuilder.builder()
                .withId(id)
                .withUserTaskId("testUserTaskId")
                .withStatus(FlowNodeStatus.APPROVED)
                .withApproved(true)
                .withOperatorId(1L)
                .withComment("testComment")
                .withExpireIntervalSeconds(1000)
                .withExternalFlowInstanceId("testExternalFlowInstanceId")
                .build();

        when(repositoryMock.findAll()).thenReturn(List.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long id = 1L;
        FlowNodeType instanceType = FlowNodeType.TEST;
        String name = "testName";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntityBuilder.builder()
                .withId(id)
                .withUserTaskId("testUserTaskId")
                .withStatus(FlowNodeStatus.APPROVED)
                .withApproved(true)
                .withOperatorId(1L)
                .withComment("testComment")
                .withExpireIntervalSeconds(1000)
                .withExternalFlowInstanceId("testExternalFlowInstanceId")
                .build();

        when(repositoryMock.findAll()).thenReturn(List.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        when(repositoryMock.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        when(repositoryMock.findById(id).get().isWaitForConfirm()).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();

        UserTaskInstanceEntity userTaskInstanceEntity1 = UserTaskInstanceEntityBuilder.builder()
                .withId(1L)
                .withUserTaskId("testUserTaskId1")
                .withStatus(FlowNodeStatus.APPROVED)
                .withApproved(true)
                .withOperatorId(1L)
                .withComment("testComment1")
                .withExpireIntervalSeconds(1000)
                .withExternalFlowInstanceId("testExternalFlowInstanceId1")
                .build();
        userTaskInstances.add(userTaskInstanceEntity1);

        UserTaskInstanceEntity userTaskInstanceEntity2 = UserTaskInstanceEntityBuilder.builder()
                .withId(2L)
                .withUserTaskId("testUserTaskId2")
                .withStatus(FlowNodeStatus.REJECTED)
                .withApproved(false)
                .withOperatorId(1L)
                .withComment("testComment2")
                .withExpireIntervalSeconds(1000)
                .withExternalFlowInstanceId("testExternalFlowInstanceId2")
                .build();
        userTaskInstances.add(userTaskInstanceEntity2);

        when(repositoryMock.findAll()).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceId, status);

        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity1, result.get(0));
    }
}