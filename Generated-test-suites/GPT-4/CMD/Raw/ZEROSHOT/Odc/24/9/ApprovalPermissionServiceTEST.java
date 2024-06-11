
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.iam.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(new ArrayList<>());

        Set<User> result = approvalPermissionService.getCandidates(2L, true);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(ids)).thenReturn(new ArrayList<>());

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(ids);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(ids)).thenReturn(new ArrayList<>());

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(ids);
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(ids)).thenReturn(new ArrayList<>());

        Set<Long> result = approvalPermissionService.getCandidateUserIds(ids);
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(new ArrayList<>());

        List<Role> result = approvalPermissionService.getCandidateRoles(2L, true);
        assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(),anySet())).thenReturn(new ArrayList<>());
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());

        boolean result = approvalPermissionService.isApprovable(2L);
        assertFalse(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        when(userTaskInstanceRepository.findByCandidateUserId(anyLong())).thenReturn(new ArrayList<>());
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        doNothing().when(userTaskCandidateRepository).save(any());

        approvalPermissionService.setCandidateResourceRole(2L, "test");
        verify(userTaskCandidateRepository, times(1)).save(any());
    }

    @Test
    public void testListApprovableExternalInstances() {
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(new ArrayList<>());

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any())).thenReturn(new ArrayList<>());

        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(ids);
        assertNotNull(result);
    }
}
