package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userTaskInstanceEntity = new UserTaskInstanceEntity();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setUserId(1L);
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>())).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setResourceRoleIdentifier("testRoleIdentifier");
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("testRoleIdentifier"))).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("testRoleIdentifier"));

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setRoleId(1L);
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setUserId(1L);
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setId(1L);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("testUserTaskId");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("testComment");
        entity.setExpireIntervalSeconds(12345);
        entity.setExternalFlowInstanceId("testExternalFlowInstanceId");

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(expectedUserTaskInstance));

        int actualResult = userTaskInstanceRepository.update(entity);

        assertEquals(1, actualResult);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("oldUserTaskId");

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int actualResult = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");

        assertEquals(1, actualResult);
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int actualResult = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        assertEquals(1, actualResult);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int actualResult = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, actualResult);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setId(1L);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", 1L))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", 1L);

        assertEquals(expectedUserTaskInstance, actualUserTaskInstance);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setId(1L);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "testName", 1L))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "testName", 1L);

        assertEquals(expectedUserTaskInstance, actualUserTaskInstance);
    }

    @Test
    public void testFindConfirmById() {
        Boolean expectedConfirm = true;

        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(expectedConfirm);

        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(1L);

        assertEquals(expectedConfirm, actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstance.setId(1L);
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED))
                .thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(expectedUserTaskInstances));

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findAll();

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindAllByFlowInstanceId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        expectedUserTaskInstances.add(expectedUserTaskInstance);

        when(userTaskInstanceRepository.findAllByFlowInstanceId(1L)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findAllByFlowInstanceId(1L);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }
}