package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2022-02-07 16:12
 * @since ODC_release_3.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Test for findByStatus method
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any(FlowNodeStatus.class))).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for findByCandidateUserIdOrRoleIds method
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any(Collection.class))).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for findByCandidateUserIdOrResourceRoleIdentifier method
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.anyLong(), Mockito.any(Collection.class))).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for findByResourceRoleIdentifierIn method
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.any(Collection.class))).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for findByRoleId method
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByRoleId(Mockito.anyLong())).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for findByCandidateUserId method
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong())).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }

    // Test for update method
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Mockito.doNothing().when(userTaskInstanceRepository).update(Mockito.any(UserTaskInstanceEntity.class));
        userTaskInstanceRepository.update(entity);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(entity);
    }

    // Test for updateUserTaskIdById method
    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";
        Mockito.doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateUserTaskIdById(id, userTaskId);
    }

    // Test for updateStatusById method
    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Mockito.doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);
        userTaskInstanceRepository.updateStatusById(id, status);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateStatusById(id, status);
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Mockito.doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(instanceId);
    }

    // Test for findByInstanceTypeAndActivityId method
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(FlowNodeType.class), Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.of(expectedUserTaskInstance));
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    // Test for findByInstanceTypeAndName method
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.any(FlowNodeType.class), Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.of(expectedUserTaskInstance));
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);
        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    // Test for findConfirmById method
    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(Mockito.anyLong())).thenReturn(confirm);
        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(1L);
        assertEquals(confirm, actualConfirm);
    }

    // Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.any(Collection.class), Mockito.any(FlowNodeStatus.class))).thenReturn(expectedUserTaskInstanceList);
        List<UserTaskInstanceEntity> actualUserTaskInstanceList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(expectedUserTaskInstanceList, actualUserTaskInstanceList);
    }
}