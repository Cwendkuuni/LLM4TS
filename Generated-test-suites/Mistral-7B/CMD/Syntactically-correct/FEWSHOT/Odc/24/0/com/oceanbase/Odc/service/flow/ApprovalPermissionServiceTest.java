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
    public void testGetCandidates_enabled_true() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(0, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean());
        verifyNoMoreInteractions(userTaskCandidateRepository);
    }

    @Test
    public void testGetCandidates_enabled_false() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(0).setRoleId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Boolean enabled = false;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository, times(1)).findByRoleIds(anyLong(), anyBoolean());
        verify(userRoleRepository, times(1)).findByRoleId(anyLong());
        verifyNoMoreInteractions(userService, userRepository, userTaskCandidateRepository);
    }

    @Test
    public void testGetCandidates_enabled_null() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(0).setRoleId(1L);
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(1).setUserId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(2, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verifyNoMoreInteractions(roleRepository, userRoleRepository, userService, userRepository);
    }

    @Test
    public void testGetCandidates_roleIds_not_empty() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(0).setRoleId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(0, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(approvalInstanceId, anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(approvalInstanceId, anyCollection(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verifyNoMoreInteractions(roleRepository, userRoleRepository, userService, userRepository);
    }

    @Test
    public void testGetCandidates_userId_not_empty() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(0).setUserId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(1, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(approvalInstanceId, anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(approvalInstanceId, anyCollection(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verifyNoMoreInteractions(roleRepository, userRoleRepository, userService, userRepository);
    }

    @Test
    public void testGetCandidates_mix_users_and_roles() {
        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean())).thenReturn(new ArrayList<>());
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(0).setRoleId(1L);
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.get(1).setUserId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(entities);

        Long approvalInstanceId = 1L;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(2, candidates.size());

        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
        verifyNoMoreInteractions(roleRepository, userRoleRepository, userService, userRepository);
    }

    // Add more test cases for other methods in the ApprovalPermissionService class
}