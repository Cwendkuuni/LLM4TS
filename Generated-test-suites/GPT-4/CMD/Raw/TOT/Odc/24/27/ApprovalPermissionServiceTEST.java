import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService service;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {}
    
    @Test
    public void testGetInstanceId2CandidateRoleIds() {}

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {}

    @Test
    public void testGetCandidateUserIds() {}

    @Test
    public void testGetCandidateRoles() {}

    @Test
    public void testIsApprovable() {}

    @Test
    public void testGetApprovableApprovalInstances() {}

    @Test
    public void testGetApprovableApprovalInstancesForGivenUserId() {}

    @Test
    public void testSetCandidateResourceRole() {}

    @Test
    public void testListApprovableExternalInstances() {}

    @Test
    public void testGetCandidatesByFlowInstanceIds() {}
}