package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity testEntity;

    @Before
    public void setup() {
        testEntity = new UserTaskInstanceEntity();
        testEntity.setFlowInstanceId(1L);
        testEntity.setUserTaskId("taskId");
        testEntity.setStatus(FlowNodeStatus.APPROVED);
        testEntity.setApproved(true);
        testEntity.setOperatorId(1L);
        testEntity.setComment("Comment");
        testEntity.setExpireIntervalSeconds(10L);
        testEntity.setExternalFlowInstanceId("externalId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setStatus(FlowNodeStatus.DRAFT);
        entities.get(1).setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(1)));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setCandidateUserId(1L);
        entities.get(0).setCandidateRoleIds(Arrays.asList(1L, 2L));
        entities.get(1).setCandidateUserId(1L);
        entities.get(1).setCandidateRoleIds(Arrays.asList(2L));
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setCandidateUserId(1L);
        entities.get(0).setResourceRoleIdentifier("roleIdentifier1");
        entities.get(1).setCandidateUserId(1L);
        entities.get(1).setResourceRoleIdentifier("roleIdentifier2");
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("roleIdentifier1"));
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setResourceRoleIdentifier("roleIdentifier1");
        entities.get(1).setResourceRoleIdentifier("roleIdentifier1");
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("roleIdentifier1"));
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setRoleId(1L);
        entities.get(1).setRoleId(1L);
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        entities.get(0).setCandidateUserId(1L);
        entities.get(1).setCandidateUserId(1L);
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findOne(1L);
        assertNotNull(entity);

        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(entity.getId());
        updatedEntity.setUserTaskId("updatedTaskId");
        updatedEntity.setStatus(FlowNodeStatus.DRAFT);
        updatedEntity.setApproved(false);
        updatedEntity.setOperatorId(2L);
        updatedEntity.setComment("Updated comment");
        updatedEntity.setExpireIntervalSeconds(5L);
        updatedEntity.setExternalFlowInstanceId("updatedExternalId");

        int updated = userTaskInstanceRepository.update(updatedEntity);
        assertTrue(updated > 0);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findOne(1L);
        assertTrue(result.getUserTaskId().equals(updatedEntity.getUserTaskId()));
        assertTrue(result.getStatus().equals(updatedEntity.getStatus()));
        assertTrue(result.isApproved().equals(updatedEntity.isApproved()));
        assertTrue(result.getOperatorId().equals(updatedEntity.getOperatorId()));
        assertTrue(result.getComment().equals(updatedEntity.getComment()));
        assertTrue(result.getExpireIntervalSeconds().equals(updatedEntity.getExpireIntervalSeconds()));
        assertTrue(result.getExternalFlowInstanceId()
                .equals(updatedEntity.getExternalFlowInstanceId()));
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String oldUserTaskId = "oldUserTaskId";
        String newUserTaskId = "newUserTaskId";

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setUserTaskId(oldUserTaskId);
        userTaskInstanceRepository.save(entity);

        int updated = userTaskInstanceRepository.updateUserTaskIdById(id, newUserTaskId);
        assertTrue(updated > 0);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findOne(id);
        assertTrue(result.getUserTaskId().equals(newUserTaskId));
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus oldStatus = FlowNodeStatus.APPROVED;
        FlowNodeStatus newStatus = FlowNodeStatus.REJECTED;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setStatus(oldStatus);
        userTaskInstanceRepository.save(entity);

        int updated = userTaskInstanceRepository.updateStatusById(id, newStatus);
        assertTrue(updated > 0);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findOne(id);
        assertTrue(result.getStatus().equals(newStatus));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(instanceId);
        userTaskInstanceRepository.save(entity);

        int deleted = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertTrue(deleted > 0);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndFlowInstanceId(
                FlowNodeType.USER_TASK, instanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long instanceId = 1L;
        String activityId = "activityId";
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(instanceId);
        entity.setInstanceType(FlowNodeType.USER_TASK);
        entity.setActivityId(activityId);
        userTaskInstanceRepository.save(entity);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.USER_TASK, activityId, instanceId);
        assertTrue(result.isPresent());
        assertTrue(result.get().equals(entity));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long instanceId = 1L;
        String name = "name";
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(instanceId);
        entity.setInstanceType(FlowNodeType.USER_TASK);
        entity.setName(name);
        userTaskInstanceRepository.save(entity);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.USER_TASK, name, instanceId);
        assertTrue(result.isPresent());
        assertTrue(result.get().equals(entity));
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        entity.setConfirm(confirm);
        userTaskInstanceRepository.save(entity);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertTrue(result.equals(confirm));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());
        entities.get(0).setFlowInstanceId(flowInstanceId);
        entities.get(0).setStatus(FlowNodeStatus.APPROVED);
        entities.get(1).setFlowInstanceId(flowInstanceId);
        entities.get(1).setStatus(FlowNodeStatus.DRAFT);
        userTaskInstanceRepository.saveAll(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(flowInstanceId), FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).equals(entities.get(0)));
    }
}