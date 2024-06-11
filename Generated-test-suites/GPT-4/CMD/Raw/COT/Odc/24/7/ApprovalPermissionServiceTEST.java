import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;


public class ApprovalPermissionServiceTest {
    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userService;

    @Mock
    ResourceRoleService resourceRoleService;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));
        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));
        List<Role> result = approvalPermissionService.getCandidateRoles(1L, true);
        assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        boolean result = approvalPermissionService.isApprovable(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> flowInstanceIds = new HashSet<>();
        flowInstanceIds.add(1L);
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertNotNull(result);
    }
}