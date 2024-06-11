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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@RunWith(MockitoJUnitRunner.class)
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
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        // Given
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setRoleId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {
                    {
                        add(candidateEntity);
                    }
                });

        Role role = new Role();
        role.setId(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled))
                .thenReturn(roles);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user = new User();
        user.setId(1L);
        expectedCandidates.add(user);
        assertEquals(expectedCandidates, candidates);

        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIdsAndEnabled(Collections.singleton(1L), enabled);
    }

    @Test
    public void testGetCandidates_DisabledRole() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;

        // Given
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setRoleId(1L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<UserTaskInstanceCandidateEntity>() {
                    {
                        add(candidateEntity);
                    }
                });

        Role role = new Role();
        role.setId(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled))
                .thenReturn(roles);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = Collections.emptySet();
        assertEquals(expectedCandidates, candidates);

        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIdsAndEnabled(Collections.singleton(1L), enabled);
    }

    @Test
    public void testGetCandidates_NoRoleOrUser() {
        Long approvalInstanceId = 1L;
        Boolean enabled = null;

        // Given
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(new ArrayList<>());
        when(roleRepository.findByRoleIdsAndEnabled(Collections.emptySet(), enabled))
                .thenReturn(Collections.emptyList());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertEquals(Collections.emptySet(), candidates);

        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIdsAndEnabled(Collections.emptySet(), enabled);
    }

    // Assuming similar test methods for other public methods in the class...
}