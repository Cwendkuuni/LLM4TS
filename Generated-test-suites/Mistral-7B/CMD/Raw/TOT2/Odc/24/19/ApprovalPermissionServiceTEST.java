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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.common.lang.Pair;
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

@SuppressWarnings("unchecked")
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
    public void testGetCandidates_enabledTrue() {
        // Given
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entity1.setRoleId(2L);
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(2L);
        entities.add(entity1);
        entities.add(entity2);

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), true))
                .thenReturn(Collections.singletonList(new Role()));

        // When
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        Set<User> expected = new HashSet<>(Collections.singletonList(new User()));
        assertEquals(expected, result);

        // Verify
        verify(userService).getCurrentUserRoleIds();
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), true);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetCandidates_enabledFalse() {
        // Given
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entity1.setRoleId(2L);
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(2L);
        entities.add(entity1);
        entities.add(entity2);

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), false))
                .thenReturn(Collections.emptyList());

        when(userRepository.findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), false))
                .thenReturn(Collections.singletonList(new User()));

        // When
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, false);

        // Then
        Set<User> expected = new HashSet<>(Collections.singletonList(new User()));
        assertEquals(expected, result);

        // Verify
        verify(userService).getCurrentUserRoleIds();
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), false);
        verify(userRepository).findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(2L)), false);
    }

    @Test
    public void testGetCandidates_enabledNull() {
        // Given
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entity1.setRoleId(2L);
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(2L);
        entities.add(entity1);
        entities.add(entity2);

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(roleRepository.findByRoleIds(new HashSet<>(Collections.singleton(2L)))).thenReturn(
                Collections.singletonList(new Role()));

        // When
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);

        // Then
        Set<User> expected = new HashSet<>(Collections.singletonList(new User()));
        assertEquals(expected, result);

        // Verify
        verify(userService).getCurrentUserRoleIds();
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findByRoleIds(new HashSet<>(Collections.singleton(2L)));
        verifyNoMoreInteractions(userRepository);
    }

    // Add more test cases for other methods in the class

    // Test edge cases and exception scenarios
}