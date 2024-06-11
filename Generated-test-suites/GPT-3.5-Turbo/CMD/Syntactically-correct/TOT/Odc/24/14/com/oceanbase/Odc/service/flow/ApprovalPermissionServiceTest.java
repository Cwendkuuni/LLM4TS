import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;


@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {
        // Mocking required behavior for tests
        // Mocking user task instance candidate entities
        Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, "ROLE"));
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(entities);

        // Mocking user task instance entities
        Set<UserTaskInstanceEntity> taskInstances = new HashSet<>();
        taskInstances.add(new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.EXECUTING));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(taskInstances);

        // Mocking user entities
        Set<UserEntity> userEntities = new HashSet<>();
        userEntities.add(new UserEntity(1L, "user1"));
        when(userRepository.findByUserIds(Collections.singleton(1L))).thenReturn(userEntities);

        // Mocking role entities
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "role1"));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), true)).thenReturn(roles);

        // Mocking user role entities
        when(userRoleRepository.findByRoleIdIn(Collections.singleton(1L))).thenReturn(Collections.singleton(new UserRoleEntity(1L, 1L)));

        // Mocking user resource role entities
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Mockito.anySet())).thenReturn(Collections.singleton(new UserResourceRoleEntity(1L, 1L)));
    }

    @Test
    public void testGetCandidates() {
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, users.size());
        assertTrue(users.stream().anyMatch(user -> user.getId() == 1L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        Set<Long> ids = approvalPermissionService.getApprovableApprovalInstances().stream().map(UserTaskInstanceEntity::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1L));
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> flowInstanceIds = new HashSet<>();
        flowInstanceIds.add(1L);
        flowInstanceIds.add(2L);
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING))
                .thenReturn(Collections.singleton(new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.EXECUTING)));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Mockito.anySet())).thenReturn(Collections.singleton(new UserResourceRoleEntity(1L, 1L)));

        Set<UserEntity> userEntities = new HashSet<>();
        userEntities.add(new UserEntity(1L, "user1"));
        when(userRepository.findByUserIds(Mockito.anySet())).thenReturn(userEntities);

        assertEquals(1, approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds).size());
    }
}