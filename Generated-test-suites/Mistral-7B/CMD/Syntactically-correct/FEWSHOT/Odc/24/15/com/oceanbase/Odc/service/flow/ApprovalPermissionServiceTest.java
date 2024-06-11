package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
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
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ApprovalPermissionServiceTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

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
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singleton(1L));
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong())).thenReturn(Collections.singletonList("1"));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity(), new UserTaskInstanceCandidateEntity()));
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Collections.singletonList(new UserEntity()));

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertEquals(1, candidates.size());
        verify(userRepository).findByRoleIdsAndEnabled(anySet(), anyBoolean());
    }

    // Test other methods with similar structure

}