package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.testutils.TestUtil;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(10);
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findAll();
        List<UserTaskInstanceEntity> statusUserTaskInstances = repository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(10, foundUserTaskInstances.size());
        assertEquals(3, statusUserTaskInstances.size());
        assertEquals(Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1), userTaskInstances.get(2)), statusUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);

        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList();
        if (userId != null) {
            expectedUserTaskInstances = Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(2), userTaskInstances.get(4));
        }

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList();
        if (userId != null) {
            expectedUserTaskInstances = Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(2), userTaskInstances.get(4));
        }

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);

        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList();
        expectedUserTaskInstances.add(userTaskInstances.get(0));
        expectedUserTaskInstances.add(userTaskInstances.get(1));

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);

        Long roleId = 1L;
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByRoleId(roleId);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Collections.emptyList();
        if (roleId != null) {
            expectedUserTaskInstances = Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1), userTaskInstances.get(3));
        }

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);

        Long userId = 1L;
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByCandidateUserId(userId);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Collections.emptyList();
        if (userId != null) {
            expectedUserTaskInstances = Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(2), userTaskInstances.get(4));
        }

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long id = entity.getId();
        UserTaskInstanceEntity updatedEntity = TestUtil.createUpdatedUserTaskInstanceEntity();

        int rowsAffected = repository.update(updatedEntity);
        UserTaskInstanceEntity foundEntity = repository.findById(id).orElse(null);

        assertEquals(1, rowsAffected);
        assertEquals(updatedEntity, foundEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long id = entity.getId();
        String userTaskId = "userTaskId";

        int rowsAffected = repository.updateUserTaskIdById(id, userTaskId);
        UserTaskInstanceEntity foundEntity = repository.findById(id).orElse(null);

        assertEquals(1, rowsAffected);
        assertEquals(userTaskId, foundEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long id = entity.getId();
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = repository.updateStatusById(id, status);
        UserTaskInstanceEntity foundEntity = repository.findById(id).orElse(null);

        assertEquals(1, rowsAffected);
        assertEquals(status, foundEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long flowInstanceId = entity.getFlowInstanceId();

        int rowsAffected = repository.deleteByFlowInstanceId(flowInstanceId);
        int count = (int) repository.count();

        assertEquals(1, rowsAffected);
        assertEquals(0, count);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long flowInstanceId = entity.getFlowInstanceId();
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activityId";

        Optional<UserTaskInstanceEntity> foundEntity = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertTrue(foundEntity.isPresent());
        assertEquals(entity, foundEntity.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long flowInstanceId = entity.getFlowInstanceId();
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "name";

        Optional<UserTaskInstanceEntity> foundEntity = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertTrue(foundEntity.isPresent());
        assertEquals(entity, foundEntity.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long id = entity.getId();

        Boolean confirm = repository.findConfirmById(id);

        assertFalse(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        UserTaskInstanceEntity entity = TestUtil.createUserTaskInstanceEntity();

        Long flowInstanceId = entity.getFlowInstanceId();
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(entity);

        assertEquals(expectedUserTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(expectedUserTaskInstances));
    }

    @Test
    public void testFindAll() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(5);
        repository.saveAll(userTaskInstances);

        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findAll();

        assertEquals(userTaskInstances.size(), foundUserTaskInstances.size());
        assertTrue(foundUserTaskInstances.containsAll(userTaskInstances));
    }

    @Test
    public void testFindAllByPage() {
        UserTaskInstanceRepository repository = TestUtil.getUserTaskInstanceRepository();
        List<UserTaskInstanceEntity> userTaskInstances = TestUtil.createUserTaskInstances(10);
        repository.saveAll(userTaskInstances);

        Pageable pageable = TestUtil.createPageable(0, 2);
        Page<UserTaskInstanceEntity> page = repository.findAll(pageable);

        List<UserTaskInstanceEntity> foundUserTaskInstances = page.getContent();

        assertEquals(2, foundUserTaskInstances.size());
        assertNotEquals(userTaskInstances.get(0), foundUserTaskInstances.get(0));
        assertNotEquals(userTaskInstances.get(1), foundUserTaskInstances.get(1));
        assertTrue(foundUserTaskInstances.containsAll(Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1))));
    }
}