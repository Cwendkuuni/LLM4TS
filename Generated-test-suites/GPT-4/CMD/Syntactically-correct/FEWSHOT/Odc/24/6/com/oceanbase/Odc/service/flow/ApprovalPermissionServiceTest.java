package com.oceanbase.odc.service.flow;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
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

import java.util.*;
import static java.util.Arrays.asList;

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
        Boolean enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, approvalInstanceId,1L, "ROLE_USER_APPROVAL", 2L, 10, "Execute"));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(new ArrayList<>());

        approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        Long approvalInstanceId = 1L;
        entities.add(new UserTaskInstanceCandidateEntity(1L, approvalInstanceId,2L, "ROLE_USER_APPROVAL", 2L, 10, "Execute"));
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    // Continue the rest of your test cases in similar fashion...
    // Remember to clean up any setup or extra objects you create for your tests 
}