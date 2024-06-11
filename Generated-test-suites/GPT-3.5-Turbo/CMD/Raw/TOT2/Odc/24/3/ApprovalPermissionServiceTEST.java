import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
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
    private UserResourceRoleRepository userResourceRoleRepository;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @Test
    public void testGetCandidates() {
        ApprovalPermissionService service = new ApprovalPermissionService();
        Set<User> expected = new HashSet<>();
        User user1 = new User();
        User user2 = new User();
        expected.add(user1);
        expected.add(user2);
        
        // Mocking
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity()));
        Mockito.when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.anyBoolean()))
                .thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        Set<User> result = service.getCandidates(123L, true);

        assertEquals(expected, result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        ApprovalPermissionService service = new ApprovalPermissionService();
        Map<Long, Set<Long>> expected = Collections.singletonMap(123L, new HashSet<>(Collections.singletonList(456L)));

        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(new UserTaskInstanceCandidateEntity());
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(Mockito.anyCollection()))
                .thenReturn(entities);

        Map<Long, Set<Long>> result = service.getInstanceId2CandidateRoleIds(Collections.singletonList(123L));

        assertEquals(expected, result);
    }

    // Add more test cases for other methods

}