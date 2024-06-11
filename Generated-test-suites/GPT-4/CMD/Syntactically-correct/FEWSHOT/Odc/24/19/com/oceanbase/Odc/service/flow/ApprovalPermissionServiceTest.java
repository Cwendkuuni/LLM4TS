package com.oceanbase.odc.service.flow;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;

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
        approvalPermissionService = new ApprovalPermissionService(userService, userTaskCandidateRepository, userTaskInstanceRepository, userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        Assert.assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        Boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();

        Assert.assertNotNull(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "Role";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        Assert.assertNotNull(result);
    }
}