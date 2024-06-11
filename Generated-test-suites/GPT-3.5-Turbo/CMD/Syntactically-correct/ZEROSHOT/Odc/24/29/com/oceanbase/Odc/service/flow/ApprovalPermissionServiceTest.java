package com.oceanbase.odc.service.flow;

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
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void setUp() {
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<>(Collections.singletonList("RESOURCE_ROLE")));
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity(1L, 1L, 0L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity(2L, 0L, 1L);
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.singletonList(new User(1L, "User1")));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new User(1L, "User1")));
    }

    // Add more test methods for other functions in ApprovalPermissionService

}