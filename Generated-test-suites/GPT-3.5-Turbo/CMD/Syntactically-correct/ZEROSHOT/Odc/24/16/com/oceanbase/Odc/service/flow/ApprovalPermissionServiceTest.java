package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        approvalPermissionService = new ApprovalPermissionService();
        approvalPermissionService.userService = userService;
        approvalPermissionService.resourceRoleService = resourceRoleService;
        approvalPermissionService.authenticationFacade = authenticationFacade;
        approvalPermissionService.userTaskCandidateRepository = userTaskCandidateRepository;
        approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
        approvalPermissionService.userRepository = userRepository;
        approvalPermissionService.roleRepository = roleRepository;
        approvalPermissionService.userRoleRepository = userRoleRepository;
        approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> expectedUsers = new HashSet<>();
        // Add expected Users

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
        when(userRepository.findByRoleIdsAndEnabled(any(), any())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(any(), any())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(any(), any(), any())).thenReturn(new ArrayList<>());

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        Map<Long, Set<Long>> expectedMap = new HashMap<>();
        // Add expected mapping

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(expectedMap, actualMap);
    }

    // Add more test cases for the remaining methods

}