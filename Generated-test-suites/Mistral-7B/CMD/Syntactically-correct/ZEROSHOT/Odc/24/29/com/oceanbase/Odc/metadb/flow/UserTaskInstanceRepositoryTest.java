import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(1000L);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L)))
                .thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("roleIdentifier")))
                .thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByResourceRoleIdentifierIn(Collections.singletonList("roleIdentifier"));
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByRoleId(1L);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserId(1L);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testUpdate() {
        int result = 1;
        Mockito.when(userTaskInstanceRepository.update(Mockito.any(UserTaskInstanceEntity.class))).thenReturn(result);
        int actualResult = userTaskInstanceRepositoryMock.update(userTaskInstanceEntity);
        assertEquals(result, actualResult);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = 1;
        Mockito.when(userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId")).thenReturn(result);
        int actualResult = userTaskInstanceRepositoryMock.updateUserTaskIdById(1L, "userTaskId");
        assertEquals(result, actualResult);
    }

    @Test
    public void testUpdateStatusById() {
        int result = 1;
        Mockito.when(userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED)).thenReturn(result);
        int actualResult = userTaskInstanceRepositoryMock.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(result, actualResult);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = 1;
        Mockito.when(userTaskInstanceRepository.deleteByFlowInstanceId(1L)).thenReturn(result);
        int actualResult = userTaskInstanceRepositoryMock.deleteByFlowInstanceId(1L);
        assertEquals(result, actualResult);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        boolean result = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(result);
        boolean actualResult = userTaskInstanceRepositoryMock.findConfirmById(1L);
        assertEquals(result, actualResult);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
                FlowNodeStatus.APPROVED)).thenReturn(list);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }
}