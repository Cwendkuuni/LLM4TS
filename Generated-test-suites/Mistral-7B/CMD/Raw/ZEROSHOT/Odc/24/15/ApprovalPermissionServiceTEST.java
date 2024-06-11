import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

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

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singleton(1L));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.singleton("test"));
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(Collections.singleton(new Role()));
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(Collections.singleton(new User()));
        when(roleRepository.findByRoleIds(anyLong())).thenReturn(Collections.singleton(new Role()));
        when(userRepository.findByUserIds(anyLong()))
                .thenReturn(Collections.singleton(new User()));
        when(userRoleRepository.findByRoleIdIn(any()))
                .thenReturn(Collections.singleton(new UserRoleEntity()));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(any(), any()))
                .thenReturn(Collections.singleton(new UserResourceRoleEntity()));
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(any(), any()))
                .thenReturn(new HashMap<>());
        when(authenticationFacade.currentUserId()).thenReturn(1L);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(1, users.size());
        verify(userRepository).findByRoleIdsAndEnabled(anyLong(), true);
        verify(userRepository, never()).findByUserIdsAndEnabled(anyLong(), null);
    }

    @Test
    public void testGetCandidatesDisabled() {
        Long approvalInstanceId = 1L;
        Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertEquals(0, users.size());
        verify(userRepository).findByRoleIdsAndEnabled(anyLong(), false);
        verify(userRepository, never()).findByRoleIdsAndEnabled(anyLong(), null);
    }

    @Test
    public void testGetCandidatesBoth() {
        Long approvalInstanceId = 1L;
        Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(2, users.size());
        verify(userRepository).findByRoleIdsAndEnabled(anyLong(), isA(Boolean.class));
        verify(userRepository).findByUserIdsAndEnabled(anyLong(), isA(Boolean.class));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCandidateUserIds() {
        List<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        List<Role> roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertEquals(1, roles.size());
        verify(roleRepository).findByRoleIdsAndEnabled(anyLong(), enabled);
        verify(roleRepository, never()).findByRoleIds(anyLong());
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verify(userTaskInstanceRepository, never()).findByStatus(FlowNodeStatus.COMPLETED);
    }

    @Test
    public void testIsApprovableFalse() {
        Long approvalInstanceId = 1L;
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verify(userTaskInstanceRepository, never()).findByStatus(FlowNodeStatus.COMPLETED);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(1, result.size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "test";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        verify(userTaskCandidateRepository).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertEquals(1, result.size());
    }
}