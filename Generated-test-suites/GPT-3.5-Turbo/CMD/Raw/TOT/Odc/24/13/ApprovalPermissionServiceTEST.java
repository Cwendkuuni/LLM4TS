import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_WithNullEnabled() {
        Long approvalInstanceId = 1L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setUserId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setRoleId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(List.of(entity1, entity2));

        Set<User> expectedUsers = Set.of(new User(entity1.getUserId()), new User(entity2.getRoleId()));
        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);

        assertEquals(expectedUsers, actualUsers);
    }

    // Additional test cases can be added here...

}