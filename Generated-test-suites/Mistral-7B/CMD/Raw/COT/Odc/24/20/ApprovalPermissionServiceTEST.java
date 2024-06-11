import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        expectedUsers.add(new User(user1));
        expectedUsers.add(new User(user2));

        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());
        when(userRepository.findByRoleIdsAndEnabled(anySet(), enabled)).thenReturn(Arrays.asList(user1, user2));

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, actualUsers);

        verify(userService).getCurrentUserRoleIds();
        verify(userRepository).findByRoleIdsAndEnabled(anySet(), enabled);
    }

    // Add more test cases for other methods in the class
}