package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryImpl;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntityImpl;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityImpl;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryImpl;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

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
    @Mock
    private ResourceRoleService resourceRoleService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private UserService userService;

    @Autowired
    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        // Given
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entities.add(entity1);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user = new UserEntityImpl();
        user.setId(1L);
        expectedCandidates.add(user);
        Role role = new RoleEntityImpl();
        role.setId(1L);
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);
        User userWithRole = new UserEntityImpl();
        userWithRole.setId(1L);
        userWithRole.setRoleIds(Collections.singletonList(role.getId()));
        expectedCandidates.add(userWithRole);
        Set<User> actualCandidates = candidates;
        assertEquals(expectedCandidates, actualCandidates);
    }

    @Test
    public void testGetCandidatesEmpty() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = null;

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertTrue(candidates.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetCandidatesNullApprovalInstanceId() {
        // Given
        Boolean enabled = true;

        // When
        approvalPermissionService.getCandidates(null, enabled);

        // Then
        // This test should throw a NullPointerException
    }

    // ... (Add similar test cases for other methods in the class)

}