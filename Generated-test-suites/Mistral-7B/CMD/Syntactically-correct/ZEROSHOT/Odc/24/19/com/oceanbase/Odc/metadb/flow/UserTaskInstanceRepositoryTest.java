import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

/**
 * Test class for {@link UserTaskInstanceRepository}
 *
 * @author yh263208
 * @date 2023-03-27 10:12
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
     */
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.APPROVED);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrResourceRoleIdentifier(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2")))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifierIn(Collection)}
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
     */
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByRoleId(1L);

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
     */
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserId(1L);

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
     */
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).update(entity);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
     */
    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(1L, "userTaskId");
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
     */
    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(1L, FlowNodeStatus.APPROVED);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
     */
    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "activityId", 1L);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
        verify(userTaskInstanceRepository, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "name", 1L);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
        verify(userTaskInstanceRepository, times(1))
                .findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
     */
    @Test
    public void testFindConfirmById() {
        boolean result = userTaskInstanceRepository.findConfirmById(1L);

        verify(userTaskInstanceRepository, times(1)).findConfirmById(1L);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L),
                FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        assertEquals(userTaskInstanceEntities, result);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
    }
}