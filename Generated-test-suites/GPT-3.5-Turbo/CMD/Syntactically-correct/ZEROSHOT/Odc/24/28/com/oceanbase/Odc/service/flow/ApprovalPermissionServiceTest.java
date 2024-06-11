package com.oceanbase.odc.service.flow;

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
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


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
    public void setUp() {
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
        // Mock data
        Long approvalInstanceId = 123L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setUserId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setRoleId(2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity1, entity2);
        
        // Mock behavior
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        // Test method
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Verify behavior
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
        assertEquals(2, candidates.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Mock data
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(1L);
        entity1.setRoleId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(2L);
        entity2.setRoleId(2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity1, entity2);

        // Mock behavior
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        // Test method
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Verify behavior
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
        assertEquals(1, result.get(1L).size());
        assertEquals(1, result.get(2L).size());
    }

    // Additional test methods can be added here

}