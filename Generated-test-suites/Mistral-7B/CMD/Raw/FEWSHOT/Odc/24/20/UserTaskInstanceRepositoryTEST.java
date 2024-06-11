package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTests {

    // Assuming UserTaskInstanceEntity is mocked appropriately

    @Test
    public void testFindByStatus() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> statusList = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(statusList);
        assertEquals(statusList, userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceList.get(0), userTaskInstanceList.get(1));
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection()))
                .thenReturn(expectedList);
        assertEquals(expectedList,
                userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceList.get(0), userTaskInstanceList.get(1));
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), anyCollection()))
                .thenReturn(expectedList);
        assertEquals(expectedList,
                userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.addAll(userTaskInstanceList);
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(anyCollection()))
                .thenReturn(expectedList);
        assertEquals(expectedList, userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(userTaskInstanceList);
        assertEquals(userTaskInstanceList, userTaskInstanceRepository.findByRoleId(1L));
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(userTaskInstanceList);
        assertEquals(userTaskInstanceList, userTaskInstanceRepository.findByCandidateUserId(1L));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.save(entity)).thenReturn(entity);
        int updatedRows = userTaskInstanceRepository.update(entity);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        Long id = 1L;
        String userTaskId = "userTaskId";
        when(userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId)).thenReturn(1);
        assertEquals(1, userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId));
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        when(userTaskInstanceRepository.updateStatusById(id, status)).thenReturn(1);
        assertEquals(1, userTaskInstanceRepository.updateStatusById(id, status));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        Long instanceId = 1L;
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        assertTrue(userTaskInstanceOptional.isPresent());
        assertEquals(userTaskInstanceEntity, userTaskInstanceOptional.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        assertTrue(userTaskInstanceOptional.isPresent());
        assertEquals(userTaskInstanceEntity, userTaskInstanceOptional.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(confirm);
        assertTrue(userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstanceList = new ArrayList<>();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceList);
        assertEquals(userTaskInstanceList, userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED));
    }
}