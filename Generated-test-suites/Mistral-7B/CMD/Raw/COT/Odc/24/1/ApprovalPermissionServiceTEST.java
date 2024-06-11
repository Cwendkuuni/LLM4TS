package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
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
import com.oceanbase.odc.service.iam.model.User;

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

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> expectedUsers = new HashSet<>();
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        User user = new User(userEntity);
        expectedUsers.add(user);

        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(approvalInstanceId);
        entity.setUserId(userEntity.getId());
        Mockito.when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsOrRoleIds(Mockito.anySet(), Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRoleRepository.findByRoleIdIn(Mockito.anySet()))
                .thenReturn(new ArrayList<>());
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(Mockito.anySet(),
                Mockito.anySet()))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByApprovalInstanceIds(approvalInstanceId))
                .thenReturn(new ArrayList<>());

        Mockito.when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findByUserIdsOrRoleIds(Mockito.anySet(), Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());
        Mockito.when(userRoleRepository.findByRoleIdIn(Mockito.anySet()))
                .thenReturn(new ArrayList<>());
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(Mockito.anySet(),
                Mockito.anySet()))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByApprovalInstanceIds(approvalInstanceId))
                .thenReturn(new ArrayList<>());

        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByApprovalInstanceIds(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByApprovalInstanceIds(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        Mockito.when(userTaskInstanceRepository.findByApprovalInstanceIds(approvalInstanceId))
                .thenReturn(new ArrayList<>());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertEquals(expectedUsers, candidates);
    }

    // Add test cases for other methods in the class

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(1L);
        entity1.setRoleId(1L);
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(1L);
        entity2.setRoleId(2L);
        entities.add(entity2);

        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        // When
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Then
        Map<Long, Set<Long>> expected = new HashMap<>();
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        roleIds.add(2L);
        expected.put(1L, roleIds);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in the class

    @Test
    public void testGetCandidateUserIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(1L);
        entity1.setRoleId(1L);
        entity1.setResourceRoleIdentifier("role1");
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(1L);
        entity2.setRoleId(2L);
        entity2.setResourceRoleIdentifier("role2");
        entities.add(entity2);

        List<UserResourceRoleEntity> userResourceRoleEntities = new ArrayList<>();
        UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
        userResourceRoleEntity1.setUserId(1L);
        userResourceRoleEntity1.setResourceRoleIdentifier("role1");
        userResourceRoleEntities.add(userResourceRoleEntity1);

        UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();
        userResourceRoleEntity2.setUserId(2L);
        userResourceRoleEntity2.setResourceRoleIdentifier("role2");
        userResourceRoleEntities.add(userResourceRoleEntity2);

        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                Mockito.anySet(), Mockito.anySet()))
                .thenReturn(userResourceRoleEntities);

        // When
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        // Then
        Set<Long> expected = new HashSet<>();
        expected.add(1L);
        expected.add(2L);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in the class

    @Test
    public void testGetCandidateRoles() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setRoleId(1L);
        entities.add(entity1);

        Role role = new Role();
        role.setId(1L);
        role.setName("Role1");

        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(new ArrayList<>());

        // When
        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        // Then
        List<Role> expected = new ArrayList<>();
        expected.add(role);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in the class

    @Test(expected = IllegalArgumentException.class)
    public void testIsApprovableWithEmptyApprovalInstanceIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();

        // When
        approvalPermissionService.isApprovable(approvalInstanceIds);

        // Then
        // An IllegalArgumentException should be thrown
    }

    // Add more test cases for other methods in the class
}