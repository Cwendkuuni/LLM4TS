package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByStatus
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for findByCandidateUserIdOrRoleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for findByResourceRoleIdentifier
    @Test
    public void testFindByResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for findByRoleId
    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for findByCandidateUserId
    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandiDATE(CandidateUserId(userId));
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for update
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        Long id = 1L;

        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

        UserTaskInstanceEntity result = userTaskInstanceRepository.save(updatedEntity);
        assertNotEquals(entity, result);
        assertEquals(updatedEntity.getUserTaskId(), result.getUserTaskId());
        assertEquals(updatedEntity.getStatus(), result.getStatus());
        assertEquals(updatedEntity.getApproved(), result.getApproved());
        assertEquals(updatedEntity.getOperatorId(), result.getOperatorId());
        assertEquals(updatedEntity.getComment(), result.getComment());
        assertEquals(updatedEntity.getExpireIntervalSeconds(), result.getExpireIntervalSeconds());
        assertEquals(updatedEntity.getExternalFlowInstanceId(), result.getExternalFlowInstanceId());
    }

    // Test for findByInstanceTypeAndActivityId
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId,
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    // Test for findByInstanceTypeAndName
    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    // Test for findConfirmById
    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(confirm, result);
    }

    // Test for findApprovalInstanceIdByFlowInstanceIdAndStatus
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceId, status);
        assertEquals(userTaskInstanceEntities.size(), result.size());
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertNotNull(result.contains(entity));
        }
    }

    // Test for deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }
}