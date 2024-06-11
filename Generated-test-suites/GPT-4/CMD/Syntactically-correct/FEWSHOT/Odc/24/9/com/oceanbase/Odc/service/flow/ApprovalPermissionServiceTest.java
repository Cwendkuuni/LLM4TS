
package com.oceanbase.odc.service.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<User> expectedResult = new HashSet<>(Arrays.asList(new User(1L, "John"), new User(2L, "Alice")));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new LinkedList());

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Implement similar methods as the method above for all other methods in the ApprovalPermissionService class
    }
}
