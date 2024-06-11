package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

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
    public void testGetCandidates_emptyApprovalInstanceId_returnsEmptySet() {
        ReflectionTestUtils.setFieldValue(approvalPermissionService, "userTaskCandidateRepository", mock(
                UserTaskInstanceCandidateRepository.class));

        Set<User> candidates = approvalPermissionService.getCandidates(null, null);

        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetCandidates_approvalInstanceId_returnsUsers() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entities.add(entity1);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        ReflectionTestUtils.setFieldValue(approvalPermissionService, "userTaskCandidateRepository", mock(
                UserTaskInstanceCandidateRepository.class));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(anyLong(), BooleanUtils.toBooleanObject(anyInt())))
                .thenReturn(new HashSet<>(Collections.singleton(new User(1L, "User 1"))));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new User(1L, "User 1")));
    }

    @Test
    public void testGetCandidates_approvalInstanceId_returnsUsersAndRoles() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entities.add(entity1);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        ReflectionTestUtils.setFieldValue(approvalPermissionService, "userTaskCandidateRepository", mock(
                UserTaskInstanceCandidateRepository.class));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), BooleanUtils.toBooleanObject(anyInt())))
                .thenReturn(new HashSet<>(Collections.singleton(new Role(1L, "Role 1"))));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new User(1L, "User 1")));
        assertTrue(candidates.contains(new Role(1L, "Role 1")));
    }

    @Test
    public void testGetCandidates_approvalInstanceId_returnsEmptySet() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(approvalInstanceId);
        entities.add(entity);

        ReflectionTestUtils.setFieldValue(approvalPermissionService, "userTaskCandidateRepository", mock(
                UserTaskInstanceCandidateRepository.class));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(anyLong(), BooleanUtils.toBooleanObject(anyInt())))
                .thenReturn(new HashSet<>());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

        assertTrue(candidates.isEmpty());
    }

    // Add test cases for other methods such as getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds, etc.

}