package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.service.UserTaskInstanceService;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.UserTaskInstance;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(UserTaskInstanceRepositoryTestConfig.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private UserTaskInstanceService userTaskInstanceService;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstance> expectedUserTaskInstances = Arrays.asList(
            new UserTaskInstance(1L, FlowNodeStatus.APPROVED),
            new UserTaskInstance(2L, FlowNodeStatus.REJECTED)
        );
        Mockito.when(userTaskInstanceService.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(expectedUserTaskInstances.size(), actualUserTaskInstanceEntities.size());
        for (int i = 0; i < expectedUserTaskInstances.size(); i++) {
            UserTaskInstanceEntity actualUserTaskInstanceEntity = actualUserTaskInstanceEntities.get(i);
            UserTaskInstance expectedUserTaskInstance = expectedUserTaskInstances.get(i);
            assertEquals(expectedUserTaskInstance.getId(), actualUserTaskInstanceEntity.getId());
            assertEquals(expectedUserTaskInstance.getStatus(), actualUserTaskInstanceEntity.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
            new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, "userId1", null),
            new UserTaskInstanceEntity(2L, FlowNodeStatus.APPROVED, "userId2", null),
            new UserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED, null, "roleId1")
        );
        Mockito.when(userTaskInstanceService.findByCandidateUserIdOrRoleIds(1L, Arrays.asList("userId1", "userId2")))
            .thenReturn(expectedUserTaskInstanceEntities);

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList("userId1", "userId2"));
        assertEquals(expectedUserTaskInstanceEntities.size(), actualUserTaskInstanceEntities.size());
        for (int i = 0; i < expectedUserTaskInstanceEntities.size(); i++) {
            UserTaskInstanceEntity actualUserTaskInstanceEntity = actualUserTaskInstanceEntities.get(i);
            UserTaskInstanceEntity expectedUserTaskInstanceEntity = expectedUserTaskInstanceEntities.get(i);
            if (expectedUserTaskInstanceEntity.getUserId() != null) {
                assertEquals(expectedUserTaskInstanceEntity.getUserId(), actualUserTaskInstanceEntity.getUserId());
            } else {
                assertEquals(expectedUserTaskInstanceEntity.getRoleId(), actualUserTaskInstanceEntity.getRoleId());
            }
            assertEquals(expectedUserTaskInstanceEntity.getStatus(), actualUserTaskInstanceEntity.getStatus());
        }
    }

    // Test other methods and edge cases here...

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, FlowNodeStatus.PENDING, null, null);
        Long id = userTaskInstanceEntity.getId();

        UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null);
        updatedUserTaskInstanceEntity.setId(null);

        Mockito.when(userTaskInstanceService.findById(id)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        int actualResult = userTaskInstanceRepository.update(updatedUserTaskInstanceEntity);
        assertEquals(1, actualResult);

        UserTaskInstanceEntity actualUpdatedUserTaskInstanceEntity = userTaskInstanceRepository.findById(id).get();
        assertEquals(updatedUserTaskInstanceEntity.getStatus(), actualUpdatedUserTaskInstanceEntity.getStatus());
    }

    // Test other update methods and edge cases here...

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
            new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null),
            new UserTaskInstanceEntity(2L, FlowNodeStatus.REJECTED, null, null),
            new UserTaskInstanceEntity(3L, FlowNodeStatus.PENDING, null, null)
        );
        Mockito.when(userTaskInstanceService.findAllByFlowInstanceId(flowInstanceId)).thenReturn(userTaskInstanceEntities);

        int actualResult = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(3, actualResult);

        List<UserTaskInstanceEntity> actualDeletedUserTaskInstanceEntities = userTaskInstanceRepository.findAll();
        assertEquals(0, actualDeletedUserTaskInstanceEntities.size());
    }

    // Test other delete methods and edge cases here...

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null);
        userTaskInstanceEntity.setId(1L);

        Mockito.when(userTaskInstanceService.findByInstanceTypeAndActivityId(FlowNodeType.SEQUENTIAL, "activityId", 1L))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.SEQUENTIAL, "activityId", 1L);
        assertTrue(actualUserTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity actualUserTaskInstanceEntity = actualUserTaskInstanceEntityOptional.get();
        assertEquals(userTaskInstanceEntity.getId(), actualUserTaskInstanceEntity.getId());
        assertEquals(userTaskInstanceEntity.getStatus(), actualUserTaskInstanceEntity.getStatus());
    }

    // Test other find methods and edge cases here...

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean expectedConfirm = true;

        Mockito.when(userTaskInstanceService.findConfirmById(id)).thenReturn(expectedConfirm);

        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(expectedConfirm, actualConfirm);
    }

    // Test other find methods and edge cases here...

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
            new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null),
            new UserTaskInstanceEntity(2L, FlowNodeStatus.REJECTED, null, null),
            new UserTaskInstanceEntity(3L, FlowNodeStatus.PENDING, null, null)
        );

        Mockito.when(userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L),
                FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstanceEntities.get(0).getId()));

        List<Long> actualUserTaskInstanceIds = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(1, actualUserTaskInstanceIds.size());
        assertEquals(userTaskInstanceEntities.get(0).getId(), actualUserTaskInstanceIds.get(0));
    }

    // Test other find methods and edge cases here...

}