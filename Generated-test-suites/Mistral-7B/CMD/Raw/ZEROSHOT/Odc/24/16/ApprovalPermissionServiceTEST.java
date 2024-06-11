import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void testGetCandidates() {
        // given
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> userTaskCandidateEntities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(userTaskCandidateEntities);
        String roleKey = "ROLE";
        String userKey = "USER";
        Map<String, Set<Long>> candidates = new HashMap<>();
        candidates.put(roleKey, new HashSet<>(Arrays.asList(1L, 2L)));
        candidates.put(userKey, new HashSet<>(Arrays.asList(3L, 4L)));

        when(roleRepository.findByRoleIdsAndEnabled(anyList(), anyBoolean()))
                .thenReturn(Arrays.asList(new Role(), new Role()));
        when(userRepository.findByRoleIdsAndEnabled(anyList(), anyBoolean()))
                .thenReturn(Arrays.asList(new User(), new User()));

        when(userRepository.findByUserIds(anyCollection()))
                .then(invocation -> {
                    Collection<User> users = invocation.getArgument(0);
                    return Arrays.asList(new User(), new User());
                });

        // when
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, Boolean.TRUE);

        // then
        assertEquals(4, result.size());
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(eq(Collections.singletonList(1L)), eq(Boolean.TRUE));
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(eq(Collections.singletonList(2L)), eq(Boolean.TRUE));
        verify(userRepository, times(2)).findByUserIds(anyCollection());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // given
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceCandidateEntity> userTaskCandidateEntities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(userTaskCandidateEntities);

        // when
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // then
        assertEquals(2, result.size());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // given
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceCandidateEntity> userTaskCandidateEntities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity());
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(userTaskCandidateEntities);

        // when
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);

        // then
        assertEquals(2, result.size());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    // Add more test cases for other methods in ApprovalPermissionService
}