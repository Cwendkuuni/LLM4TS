
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userService;

    @Mock
    ResourceRoleService resourceRoleService;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    UserResourceRoleRepository userResourceRoleRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>());
        Set<User> result = approvalPermissionService.getCandidates(0L, true);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new ArrayList<Long>());
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(new ArrayList<Long>());
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> result = approvalPermissionService.getCandidateUserIds(new ArrayList<Long>());
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>());
        List<Role> result = approvalPermissionService.getCandidateRoles(0L, true);
        assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        boolean result = approvalPermissionService.isApprovable(0L);
        assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstancesForUserId() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(0L);
        assertNotNull(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(0L, "resourceRoleIdentifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(new ArrayList<Long>());
        assertNotNull(result);
    }
}
