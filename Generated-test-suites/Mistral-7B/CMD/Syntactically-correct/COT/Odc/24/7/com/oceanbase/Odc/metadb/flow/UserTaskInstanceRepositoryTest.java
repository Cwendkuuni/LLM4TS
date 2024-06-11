package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2022-02-07 16:12
 */
public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.APPROVED))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.eq(1L), Mockito.eq(Collections.singletonList(1L)))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));

        assertEquals(expected, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.eq(1L), Mockito.eq(Collections.singletonList("roleIdentifier")))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("roleIdentifier"));

        assertEquals(expected, result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.eq(Arrays.asList("roleIdentifier1", "roleIdentifier2")))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier1", "roleIdentifier2"));

        assertEquals(expected, result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByRoleId(Mockito.eq(1L))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.eq(1L))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findById(Mockito.eq(1L))).thenReturn(Optional.ofNullable(entity));

        int result = userTaskInstanceRepository.update(entity);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(Mockito.any(UserTaskInstanceEntity.class));
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";
        Mockito.when(userTaskInstanceRepository.findById(Mockito.eq(id))).thenReturn(Optional.ofNullable(new UserTaskInstanceEntity()));

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateUserTaskIdById(Mockito.eq(id), Mockito.eq(userTaskId));
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Mockito.when(userTaskInstanceRepository.findById(Mockito.eq(id))).thenReturn(Optional.ofNullable(new UserTaskInstanceEntity()));

        int result = userTaskInstanceRepository.updateStatusById(id, status);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateStatusById(Mockito.eq(id), Mockito.eq(status));
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByFlowInstanceId(instanceId)).thenReturn(Collections.singletonList(new UserTaskInstanceEntity()));

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteAllInBatch();
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.eq(FlowNodeType.START), Mockito.eq("activityId"), Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(expected));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);

        assertEquals(expected, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.eq(FlowNodeType.START), Mockito.eq("name"), Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(expected));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);

        assertEquals(expected, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Boolean expected = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(Mockito.eq(1L))).thenReturn(expected);

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.eq(Arrays.asList(1L, 2L)), Mockito.eq(FlowNodeStatus.APPROVED)))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        assertEquals(expected, result);
    }
}