import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
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

public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;
    @Autowired
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserResourceRoleRepository userResourceRoleRepository;
    @Autowired
    private ResourceRoleService resourceRoleService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private Long approvalInstanceId;
    private Long flowInstanceId;
    private Long userId;
    private Long roleId;
    private String resourceRoleIdentifier;
    private Set<Long> approvalInstanceIds;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthenticationFacade authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        Mockito.when(authenticationFacade.currentUserId()).thenReturn(userId);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "authenticationFacade",
                authenticationFacadeMock);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userService", userService);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "resourceRoleService",
                resourceRoleService);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userRepository", userRepository);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "roleRepository", roleRepository);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userRoleRepository",
                userRoleRepository);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userResourceRoleRepository",
                userResourceRoleRepository);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userTaskCandidateRepository",
                userTaskCandidateRepository);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "userTaskInstanceRepository",
                userTaskInstanceRepository);

        approvalInstanceId = 1L;
        flowInstanceId = 2L;
        userId = 3L;
        roleId = 4L;
        resourceRoleIdentifier = "testRoleIdentifier";
        approvalInstanceIds = new HashSet<>(Collections.singleton(approvalInstanceId));
    }

    @Test
    public void testGetCandidates() {
        // Test with enabled = true
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(1, candidates.size());
        User candidate = candidates.iterator().next();
        assertNotNull(candidate);

        // Test with enabled = false
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertEquals(0, candidates.size());

        // Test with null enabled
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(1, candidates.size());
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertEquals(1, candidateRoles.size());
        Role role = candidateRoles.get(0);
        assertNotNull(role);
    }

    @Test
    public void testIsApprovable() {
        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertEquals(1, approvalPermissionService.getApprovableApprovalInstances().size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        UserTaskInstanceCandidateEntity candidateEntity = userTaskCandidateRepository
                .findByApprovalInstanceIdAndResourceRoleIdentifier(approvalInstanceId, resourceRoleIdentifier);
        assertNotNull(candidateEntity);
    }

    @Test
    public void testListApprovableExternalInstances() {
        assertEquals(1, approvalPermissionService.listApprovableExternalInstances().size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<UserEntity> candidates = approvalPermissionService.getCandidatesByFlowInstanceIds(approvalInstanceIds).get(approvalInstanceId);
        assertEquals(1, candidates.size());
        UserEntity userEntity = candidates.iterator().next();
        assertNotNull(userEntity);
    }
}