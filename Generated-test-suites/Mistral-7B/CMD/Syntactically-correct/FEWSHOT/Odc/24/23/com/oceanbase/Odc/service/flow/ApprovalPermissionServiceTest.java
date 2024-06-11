package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
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
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

@SuppressWarnings("unchecked")
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
        // Given
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entity1.setRoleId(1L);
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(2L);
        entities.add(entity1);
        entities.add(entity2);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        expectedCandidates.add(new User(1L, "User1", null, null, null, Collections.emptyList(), null, null, null));
        expectedCandidates.add(new User(null, "Role1", null, null, null, Collections.emptyList(), null, null, null));
        expectedCandidates.add(new User(2L, "User2", null, null, null, Collections.emptyList(), null, null, null));
        expectedCandidates.add(new User(null, "Role2", null, null, null, Collections.emptyList(), null, null, null));
        assertEquals(expectedCandidates, candidates);
    }

    // Add other test methods for getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds,
    // getCandidateUserIds, getCandidateRoles, isApprovable, getApprovableApprovalInstances, setCandidateResourceRole,
    // listApprovableExternalInstances, and getCandidatesByFlowInstanceIds methods.

}