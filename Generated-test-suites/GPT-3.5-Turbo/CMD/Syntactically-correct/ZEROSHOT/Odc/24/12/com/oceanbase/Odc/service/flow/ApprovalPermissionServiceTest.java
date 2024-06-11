import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.resourcerole.UserRoleRepository;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleEntity;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

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

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> expectedUsers = new HashSet<>(Arrays.asList(new User(), new User()));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        when(userRepository.findByRoleIdsAndEnabled(new HashSet<>(), enabled)).thenReturn(expectedUsers);
        when(userRepository.findByUserIdsAndEnabled(new HashSet<>(), enabled)).thenReturn(expectedUsers);
        when(userRepository.findByUserIdsOrRoleIds(new HashSet<>(), new HashSet<>(), enabled)).thenReturn(expectedUsers);

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertTrue(expectedUsers.containsAll(actualUsers) && actualUsers.containsAll(expectedUsers));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = Collections.singletonList(1L);

        Map<Long, Set<Long>> expectedMap = Collections.singletonMap(1L, Collections.singleton(2L));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = Collections.singletonList(1L);

        Map<Long, Set<String>> expectedMap = Collections.singletonMap(1L, Collections.singleton("role"));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);

        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetCandidateUserIds() {
        List<Long> approvalInstanceIds = Collections.singletonList(1L);

        Set<Long> expectedUserIds = new HashSet<>(Collections.singletonList(1L));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        when(userRoleRepository.findByRoleIdIn(new HashSet<>())).thenReturn(Collections.emptyList());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("role")))
                .thenReturn(Collections.singletonList(new UserResourceRoleEntity(1L, "role", 1L)));

        Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertNotNull(actualUserIds);
        assertEquals(expectedUserIds, actualUserIds);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> expectedRoles = new ArrayList<>(Collections.singletonList(new Role()));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(), enabled)).thenReturn(expectedRoles);

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(expectedRoles.get(0), actualRoles.get(0));
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        List<UserTaskInstanceEntity> approvableInstances = Collections.singletonList(new UserTaskInstanceEntity());
        when(approvalPermissionService.getApprovableApprovalInstances()).thenReturn(approvableInstances);

        boolean approvable = approvalPermissionService.isApprovable(approvalInstanceId);

        assertTrue(approvable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableInstances = Collections.singletonList(new UserTaskInstanceEntity());
        when(approvalPermissionService.getApprovableApprovalInstances()).thenReturn(approvableInstances);

        List<UserTaskInstanceEntity> actualInstances = approvalPermissionService.getApprovableApprovalInstances();

        assertNotNull(actualInstances);
        assertEquals(approvableInstances, actualInstances);
    }

}