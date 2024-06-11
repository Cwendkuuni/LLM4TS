package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.boot.test.mock.mockito.MockBean;

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

public class ApprovalPermissionServiceTest {

 @Mock
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private RoleRepository roleRepository;
 @Mock
 private UserRepository userRepository;
 @Mock
 private UserRoleRepository userRoleRepository;
 @Mock
 private UserResourceRoleRepository userResourceRoleRepository;
 @Mock
 private ResourceRoleService resourceRoleService;
 @Mock
 private AuthenticationFacade authenticationFacade;
 @MockBean
 private UserService userService;
 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;
 @AutowiredAnnotatedField("userService")
 private UserService userServiceAnnotation;

 @Before
 public void setup() {
 userServiceAnnotation.setResourceRoleService(resourceRoleService);
 }

 @Test
 public void testGetCandidates() {
 // Given
 Long approvalInstanceId = 1L;
 Set<User> expectedCandidates = new HashSet<>();
 expectedCandidates.add(new User(1L, "user1"));
 expectedCandidates.add(new User(2L, "user2"));
 Set<User> candidates = new HashSet<>();
 candidates.add(new User(3L, "user3"));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Arrays.asList(
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L), new UserTaskInstanceCandidateEntity(approvalInstanceId, 2L)));
 when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.emptyList());
 when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.singletonList(expectedCandidates.iterator().next()));
 when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singleton(1L));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(Collections.singletonList(new UserResourceRoleEntity(1L, 1L, 1L)));

 // When
 Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertEquals("Wrong number of candidates", expectedCandidates.size(), actualCandidates.size());
 assertEquals("Wrong candidate", expectedCandidates.iterator().next(), actualCandidates.iterator().next());
 }

 @Test
 public void testGetCandidatesEmpty() {
 // Given
 Long approvalInstanceId = 1L;

 // When
 Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertTrue("Candidates should be empty", actualCandidates.isEmpty());
 }

 // Add more tests here for other methods in the ApprovalPermissionService class, including edge cases and error scenarios.

}