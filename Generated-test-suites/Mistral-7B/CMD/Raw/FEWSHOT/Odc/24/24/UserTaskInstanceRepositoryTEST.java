package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        // Initialize the UserTaskInstanceRepository instance
        userTaskInstanceRepository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepository is an interface, use an implementation here
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.PENDING), new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> statusApprovedEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(2, statusApprovedEntities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, null),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.PENDING, 1L), new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED, 2L),
                new UserTaskInstanceEntity(4L, null, 1L));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> userIdEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);

        assertEquals(3, userIdEntities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, null, "Role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.PENDING, null, "Role2"),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED, 1L, "Role1"), new UserTaskInstanceEntity(4L, null, "Role3"));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> roleEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null,
                Arrays.asList("Role1"));

        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, null, "Role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.PENDING, null, "Role2"),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED, 1L, "Role1"), new UserTaskInstanceEntity(4L, null, "Role3"));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> roleEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("Role1"));

        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, null, null, 1L),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.PENDING, null, 2L),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED, null, 1L), new UserTaskInstanceEntity(4L, null, null, 3L));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> roleEntities = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, 1L, null),
                new UserTaskInstanceEntity(2L, 2L, null), new UserTaskInstanceEntity(3L, 1L, null),
                new UserTaskInstanceEntity(4L, 3L, null));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> userIdEntities = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(3, userIdEntities.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, null, null);
        Long id = 1L;

        userTaskInstanceRepository.update(userTaskInstanceEntity);

        verify(userTaskInstanceRepository).update("update UserTaskInstanceEntity as ut set ut.userTaskId=:#{#param.userTaskId},ut.status=:#{#param.status},"
                + "ut.approved=:#{#param.approved},ut.operatorId=:#{#param.operatorId},ut.comment=:#{#param.comment},"
                + "ut.expireIntervalSeconds=:#{#param.expireIntervalSeconds},ut.externalFlowInstanceId=:#{#param.externalFlowInstanceId}"
                + " where ut.id=:#{#param.id}", eq(userTaskInstanceEntity), anyMap());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        verify(userTaskInstanceRepository).update("update flow_instance_node_approval set user_task_id=:userTaskId where id=:id",
                eq(id), eq(userTaskId));
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        userTaskInstanceRepository.updateStatusById(id, status);

        verify(userTaskInstanceRepository).update("update flow_instance_node_approval set status=:#{#status.name()} where id=:id",
                eq(id), eq(status));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        verify(userTaskInstanceRepository).delete("from UserTaskInstanceEntity as ut where ut.flowInstanceId=:instanceId",
                eq(instanceId));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, null, null, null, "testActivityId", 1L);

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TEST_TYPE, "testActivityId", 1L))
                .thenReturn(optionalUserTaskInstanceEntity);

        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.TEST_TYPE, "testActivityId", 1L).get();

        assertEquals(userTaskInstanceEntity, foundUserTaskInstanceEntity);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, null, null, null, null, 1L, "testName");

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TEST_TYPE, "testName", 1L))
                .thenReturn(optionalUserTaskInstanceEntity);

        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.TEST_TYPE, "testName", 1L).get();

        assertEquals(userTaskInstanceEntity, foundUserTaskInstanceEntity);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean foundConfirm = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(confirm, foundConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, null, null, null, null, 1L,
                FlowNodeStatus.APPROVED), new UserTaskInstanceEntity(2L, null, null, null, null, 1L,
                FlowNodeStatus.PENDING), new UserTaskInstanceEntity(3L, null, null, null, null, 2L, FlowNodeStatus.APPROVED),
                new UserTaskInstanceEntity(4L, null, null, null, null, 2L, FlowNodeStatus.PENDING));
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L),
                FlowNodeStatus.APPROVED);

        assertEquals(2, entities.size());
    }
}