package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = UserTaskInstanceRepositoryTest.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity testEntity;

    @Before
    public void setup() {
        testEntity = new UserTaskInstanceEntity();
        testEntity.setId(1L);
        testEntity.setFlowInstanceId(1L);
        testEntity.setUserTaskId("UT-001");
        testEntity.setStatus(FlowNodeStatus.APPROVED);
        testEntity.setApproved(true);
        testEntity.setOperatorId(1L);
        testEntity.setComment("Test comment");
        testEntity.setExpireIntervalSeconds(3600L);
        testEntity.setExternalFlowInstanceId("EXT-001");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(testEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<String> roleIdentifiers = Arrays.asList("ROLE_001", "ROLE_002");
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);
        entity1.setUserTaskId("UT-001");
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setApproved(true);
        entity1.setOperatorId(1L);
        entity1.setComment("Test comment");
        entity1.setExpireIntervalSeconds(3600L);
        entity1.setExternalFlowInstanceId("EXT-001");
        entity1.setUserId(userId);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);
        entity2.setUserTaskId("UT-002");
        entity2.setStatus(FlowNodeStatus.APPROVED);
        entity2.setApproved(true);
        entity2.setOperatorId(1L);
        entity2.setComment("Test comment");
        entity2.setExpireIntervalSeconds(3600L);
        entity2.setExternalFlowInstanceId("EXT-002");
        entity2.setRoleId(1L);
        entities.add(entity1);
        entities.add(entity2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIdentifiers);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertEquals(userId, result.get(0).getUserId());
        assertFalse(result.get(0).isRoleBased());
        assertNull(result.get(0).getRoleId());
        assertNotNull(result.get(1));
        assertEquals(roleIdentifiers.get(0), result.get(1).getRoleIdentifier());
        assertFalse(result.get(1).isUserBased());
        assertNull(result.get(1).getUserId());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        String roleIdentifier = "ROLE_001";
        List<UserTaskInstanceEntity> entities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(roleIdentifier));
        assertEquals(1, result.size());
        assertEquals(testEntity, result.get(0));
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> entities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(1, result.size());
        assertEquals(testEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> entities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(1, result.size());
        assertEquals(testEntity, result.get(0));
    }

    @Test
    public void testUpdate() {
        Mockito.doAnswer(invocationOnMock -> {
            UserTaskInstanceEntity entity = (UserTaskInstanceEntity) invocationOnMock.getArgument(0);
            entity.setUserTaskId("UT-002");
            entity.setStatus(FlowNodeStatus.REJECTED);
            entity.setApproved(false);
            entity.setOperatorId(2L);
            entity.setComment("Test comment updated");
            entity.setExpireIntervalSeconds(7200L);
            entity.setExternalFlowInstanceId("EXT-003");
            return 1;
        }).when(userTaskInstanceRepository).update(testEntity);
        int result = userTaskInstanceRepository.update(testEntity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "UT-002";
        Mockito.doAnswer(invocationOnMock -> {
            UserTaskInstanceEntity entity = (UserTaskInstanceEntity) invocationOnMock.getArgument(0);
            entity.setUserTaskId(userTaskId);
            return 1;
        }).when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.REJECTED;
        Mockito.doAnswer(invocationOnMock -> {
            UserTaskInstanceEntity entity = (UserTaskInstanceEntity) invocationOnMock.getArgument(0);
            entity.setStatus(status);
            return 1;
        }).when(userTaskInstanceRepository).updateStatusById(id, status);
        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Mockito.doAnswer(invocationOnMock -> {
            UserTaskInstanceEntity entity = (UserTaskInstanceEntity) invocationOnMock.getArgument(0);
            return 1;
        }).when(userTaskInstanceRepository).delete(testEntity);
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "ACT-001";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "Task Name";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> entities = Arrays.asList(testEntity, new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Specification.where(UserTaskInstanceEntitySpecifications.hasFlowInstanceId(flowInstanceId))
                .and(UserTaskInstanceEntitySpecifications.hasStatus(FlowNodeStatus.APPROVED)))).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, FlowNodeStatus.APPROVED);
        assertEquals(2, result.size());
    }
}

class UserTaskInstanceEntitySpecifications {

    @SuppressWarnings("unchecked")
    public static Specification<UserTaskInstanceEntity> hasFlowInstanceId(Long flowInstanceId) {
        return (Specification<UserTaskInstanceEntity>) (root, query, cb) -> {
            return cb.equal(root.get("flowInstanceId"), flowInstanceId);
        };
    }

    @SuppressWarnings("unchecked")
    public static Specification<UserTaskInstanceEntity> hasStatus(FlowNodeStatus status) {
        return (Specification<UserTaskInstanceEntity>) (root, query, cb) -> {
            return cb.equal(root.get("status"), status);
        };
    }
}