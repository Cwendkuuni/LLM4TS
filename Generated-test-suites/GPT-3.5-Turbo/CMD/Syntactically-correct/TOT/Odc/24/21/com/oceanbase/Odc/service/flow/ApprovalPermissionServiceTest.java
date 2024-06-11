import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        // Setup mock data or dependencies here if needed
    }

    @Test
    public void testGetCandidates() {
        // Test case for getCandidates method
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> expected = new HashSet<>();
        expected.add(new User(1L, "John Doe"));
        expected.add(new User(2L, "Jane Smith"));

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, null, 1L));
        entities.add(new UserTaskInstanceCandidateEntity(2L, 2L, null));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled))).thenReturn(Arrays.asList(new User(1L, "John Doe"), new User(2L, "Jane Smith")));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in ApprovalPermissionService class

}