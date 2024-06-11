package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.Class)
public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserResourceRoleRepository userResourceRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Long approvalInstanceId;
    private Long userId;

    @BeforeEach
    public void setup() {
        // Set up user and role data for testing
        Role role = new Role();
        role.setName("TEST_ROLE");
        roleRepository.save(role);

        User user = new User();
        user.setName("TEST_USER");
        user.setEmail("test.user@example.com");
        user.setPassword("test123");
        userService.saveUser(user);

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        UserResourceRoleEntity userResourceRole = new UserResourceRoleEntity();
        userResourceRole.setUserId(user.getId());
        userResourceRole.setResourceId(1L); // Replace with actual resource ID
        userResourceRole.setResourceRoleIdentifier("TEST_ROLE_IDENTIFIER");
        userResourceRoleRepository.save(userResourceRole);

        approvalInstanceId = 1L; // Replace with actual approval instance ID
        userId = user.getId();
    }

    @Test
    public void testGetCandidates() {
        // Test getting candidates with enabled users and roles
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        Assertions.assertFalse(candidates.isEmpty());
        Assertions.assertTrue(candidates.contains(new User(userId)));

        // Test getting candidates with disabled users and roles
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
        Assertions.assertFalse(candidates.isEmpty());
        Assertions.assertFalse(candidates.contains(new User(userId)));

        // Test getting candidates with null enabled flag
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assertions.assertFalse(candidates.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test getting instance ID to candidate role ID mapping
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstanceId);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.containsKey(approvalInstanceId));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Test getting instance ID to candidate resource role identifier ID mapping
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstanceId);
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.containsKey(approvalInstanceId));
    }

    @Test
    public void testGetCandidateUserIds() {
        // Test getting candidate user IDs
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstanceId);
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.contains(userId));
    }

    @Test
    public void testGetCandidateRoles() {
        // Test getting candidate roles
        List<Role> expectedRoles = Arrays.asList(role);
        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        Assertions.assertEquals(expectedRoles, result);
    }

    @Test
    public void testIsApprovable() {
        // Test approvable approval instances
        Assertions.assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        // Test non-approvable approval instances
        Long nonApprovableApprovalInstanceId = 2L; // Replace with actual non-approvable approval instance ID
        Assertions.assertFalse(approvalPermissionService.isApprovable(nonApprovableApprovalInstanceId));
    }

    @Test
    public void testSetCandidateResourceRole() {
        // Test setting candidate resource role
        String resourceRoleIdentifier = "TEST_RESOURCE_ROLE_IDENTIFIER";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        // Verify that candidate resource role has been set
        UserTaskInstanceCandidateEntity candidateEntity = approvalPermissionService.userTaskCandidateRepository
                .findByApprovalInstanceId(approvalInstanceId)
                .get(0);
        Assertions.assertEquals(resourceRoleIdentifier, candidateEntity.getResourceRoleIdentifier());
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Test getting approvable external instances
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Test getting candidates by flow instance IDs
        Long flowInstanceId = 1L; // Replace with actual flow instance ID
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(flowInstanceId));
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.containsKey(flowInstanceId));
        Assertions.assertTrue(result.get(flowInstanceId).contains(new UserEntity(user.getId())));
    }
}