package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import java.util.*;

public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        HashSet<User> expectedResult = new HashSet<User>();
        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        ArrayList<Long> approvalInstanceIds = new ArrayList<Long>();
        assertTrue(approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds).isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        ArrayList<Long> approvalInstanceIds = new ArrayList<Long>();
        assertTrue(approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds).isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        ArrayList<Long> approvalInstanceIds = new ArrayList<Long>();
        assertTrue(approvalPermissionService.getCandidateUserIds(approvalInstanceIds).isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        ArrayList<Role> expectedResult = new ArrayList<Role>();
        List<Role> result = approvalPermissionService.getCandidateRoles(1L, true);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsApprovable() {
        assertFalse(approvalPermissionService.isApprovable(1L));
    }

     @Test
    public void testGetCandidatesByFlowInstanceIds() {
        ArrayList<Long> flowInstanceIds = new ArrayList<Long>();
        assertTrue(approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds).isEmpty());
    }
}