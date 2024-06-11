import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.oceanbase.odc.common.lang.Pair;
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
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import org.junit.Before;
import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UserResourceRoleRepository userResourceRoleRepository;
    private UserService userService;
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        userResourceRoleRepository = mock(UserResourceRoleRepository.class);
        userService = mock(UserService.class);
        authenticationFacade = mock(AuthenticationFacade.class);

        approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);
        approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
        approvalPermissionService.setUserRepository(userRepository);
        approvalPermissionService.setRoleRepository(roleRepository);
        approvalPermissionService.setUserRoleRepository(userRoleRepository);
        approvalPermissionService.setUserResourceRoleRepository(userResourceRoleRepository);
        approvalPermissionService.setUserService(userService);
        approvalPermissionService.setAuthenticationFacade(authenticationFacade);
    }

    @Test
    public void testGetCandidates() {
        // Test case 1
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setUserId(1L);
        entity1.setRoleId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setUserId(2L);
        entity2.setRoleId(2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity1, entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(userRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(Arrays.asList(new User(new UserEntity()), new User(new UserEntity())));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(2, result.size());

        // Test case 2
        enabled = false;

        when(userRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(Collections.emptyList());

        result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(0, result.size());
    }

    // Add more test methods for other ApprovalPermissionService methods

}