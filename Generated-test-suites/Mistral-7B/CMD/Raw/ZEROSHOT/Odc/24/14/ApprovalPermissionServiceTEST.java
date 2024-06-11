import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.UserRole;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ResourceRoleService resourceRoleService;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    private Long approvalInstanceId = 1L;
    private Long userId = 2L;
    private Long roleId = 3L;
    private Long flowInstanceId = 4L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUserId()).thenReturn(userId);
    }

    @Test
    public void testGetCandidates() {
        User user1 = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        Role role1 = new Role(1L, "role1");
        UserRole userRole1 = new UserRole(1L, userId, role1.getId());
        UserResourceRole userResourceRole1 = new UserResourceRole(1L, userRole1.getId(), "resource1");

        when(userRepository.findByUserIdsAndEnabled(eq(userId), anyBoolean())).thenReturn(Arrays.asList(user1, user2));
        when(roleRepository.findByRoleIdsAndEnabled(eq(roleIds), anyBoolean())).thenReturn(Collections.singletonList(role1));
        when(userRoleRepository.findByUserId(userId)).thenReturn(Collections.singletonList(userRole1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anyList(), anyList()))
                .thenReturn(Collections.singletonList(userResourceRole1));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(user1));
        assertTrue(candidates.contains(user2));
    }

    @Test
    public void testGetCandidates_disabledUsers() {
        User user1 = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        Role role1 = new Role(1L, "role1");
        UserRole userRole1 = new UserRole(1L, userId, role1.getId());
        UserResourceRole userResourceRole1 = new UserResourceRole(1L, userRole1.getId(), "resource1");

        when(userRepository.findByUserIdsAndEnabled(eq(userId), anyBoolean())).thenReturn(Arrays.asList(user1, user2));
        when(roleRepository.findByRoleIdsAndEnabled(eq(roleIds), anyBoolean())).thenReturn(Collections.singletonList(role1));
        when(userRoleRepository.findByUserId(userId)).thenReturn(Collections.singletonList(userRole1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anyList(), anyList()))
                .thenReturn(Collections.singletonList(userResourceRole1));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertEquals(1, candidates.size());
        assertFalse(candidates.contains(user2));
        assertTrue(candidates.contains(user1));
    }

    @Test
    public void testGetCandidates_noUsersOrRoles() {
        when(userRepository.findByUserIdsAndEnabled(eq(userId), anyBoolean())).thenReturn(Collections.emptyList());
        when(roleRepository.findByRoleIdsAndEnabled(eq(roleIds), anyBoolean())).thenReturn(Collections.emptyList());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(0, candidates.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId1);
        entity1.setRoleId(1L);
        entity2.setApprovalInstanceId(approvalInstanceId2);
        entity2.setRoleId(2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(eq(Arrays.asList(approvalInstanceId1,
                approvalInstanceId2)))).thenReturn(Arrays.asList(entity1, entity2));

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(approvalInstanceId1, approvalInstanceId2));
        assertEquals(2, result.size());
        assertEquals(Collections.singleton(1L), result.get(approvalInstanceId1));
        assertEquals(Collections.singleton(2L), result.get(approvalInstanceId2));
    }

    // Add more test methods for other methods in the ApprovalPermissionService class

    @Test
    public void testIsApprovable() {
        UserTaskInstanceEntity approvalInstanceEntity1 = new UserTaskInstanceEntity();
        approvalInstanceEntity1.setId(1L);
        UserTaskInstanceEntity approvalInstanceEntity2 = new UserTaskInstanceEntity();
        approvalInstanceEntity2.setId(2L);
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Arrays.asList(approvalInstanceEntity1, approvalInstanceEntity2));

        assertTrue(approvalPermissionService.isApprovable(1L));
        assertTrue(approvalPermissionService.isApprovable(2L));
    }

    // Add more test methods for other methods in the ApprovalPermissionService class
}