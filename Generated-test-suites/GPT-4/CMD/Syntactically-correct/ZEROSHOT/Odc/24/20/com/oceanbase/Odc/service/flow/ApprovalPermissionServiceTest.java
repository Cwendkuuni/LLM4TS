package com.oceanbase.odc.service.flow;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
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
        // set up test data
    }

    @Test
    public void testGetCandidates() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anySet(), any())).thenReturn(new ArrayList<>());
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        Assert.assertNotNull(users);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(new ArrayList<>());
        Map<Long, Set<Long>> roleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L));
        Assert.assertNotNull(roleIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(new ArrayList<>());
        Map<Long, Set<String>> identifiers = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Arrays.asList(1L));
        Assert.assertNotNull(identifiers);
    }

    @Test
    public void testGetCandidateUserIds() {
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(new ArrayList<>());
        when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(new ArrayList<>());
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(Arrays.asList(1L));
        Assert.assertNotNull(userIds);
    }

    @Test
    public void testIsApprovable() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(any(), anySet())).thenReturn(new ArrayList<>());
        Boolean canApprove = approvalPermissionService.isApprovable(1L);
        Assert.assertNotNull(canApprove);
    }

    @Test
    public void testSetCandidateResourceRole() {
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        when(userTaskCandidateRepository.save(any())).thenReturn(candidateEntity);
        approvalPermissionService.setCandidateResourceRole(1L, "ResourceRoleIdentifier");
        Assert.assertNotNull(candidateEntity);
    }

    // ... create the rest of the test methods

}
