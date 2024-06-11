package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
@Component
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
        Set<User> expected = new HashSet<>();
        UserEntity userEntity1 = new UserEntity();
        UserEntity userEntity2 = new UserEntity();
        Role role1 = new Role();
        Role role2 = new Role();
        UserRoleEntity userRoleEntity1 = new UserRoleEntity();
        UserRoleEntity userRoleEntity2 = new UserRoleEntity();
        UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
        UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();

        userEntity1.setId(1L);
        userEntity2.setId(2L);
        role1.setId(1L);
        role1.setName("role1");
        role2.setId(2L);
        role2.setName("role2");
        userRoleEntity1.setUserId(userEntity1.getId());
        userRoleEntity1.setRoleId(role1.getId());
        userRoleEntity2.setUserId(userEntity2.getId());
        userRoleEntity2.setRoleId(role2.getId());
        userResourceRoleEntity1.setUserId(userEntity1.getId());
        userResourceRoleEntity1.setResourceRoleIdentifier("resourceRoleIdentifier1");
        userResourceRoleEntity2.setUserId(userEntity2.getId());
        userResourceRoleEntity2.setResourceRoleIdentifier("resourceRoleIdentifier2");

        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());
        when(userRepository.findByRoleIdsAndEnabled(role1.getId(), enabled)).thenReturn(Collections.singletonList(userEntity1));
        when(userRepository.findByRoleIdsAndEnabled(role2.getId(), enabled)).thenReturn(Collections.singletonList(userEntity2));

        Set<User> actual = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        Map<Long, Set<Long>> expected = new HashMap<>();
        expected.put(1L, new HashSet<>(Collections.singleton(1L)));
        expected.put(2L, new HashSet<>(Collections.singleton(2L)));

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity4 = new UserTaskInstanceCandidateEntity();

        entity1.setApprovalInstanceId(1L);
        entity1.setRoleId(1L);
        entity2.setApprovalInstanceId(1L);
        entity2.setRoleId(2L);
        entity3.setApprovalInstanceId(2L);
        entity3.setRoleId(1L);
        entity4.setApprovalInstanceId(2L);
        entity4.setRoleId(2L);

        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<Long>> actual = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expected, actual);
    }

    // Add test cases for other methods in ApprovalPermissionService, such as getInstanceId2CandidateResourceRoleIdentifierIds, getCandidateUserIds, getCandidateRoles, isApprovable, getApprovableApprovalInstances, setCandidateResourceRole, listApprovableExternalInstances, and getCandidatesByFlowInstanceIds.

}