
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    private Long approvalInstanceId = 1L;
    private Boolean enabled = true;
    
    @Before
    public void setUp() {
        // Instantiate the classes and set up the dependencies
        approvalPermissionService = new ApprovalPermissionService();
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

        // Wire up the dependencies
        approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);
        approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
    }

    @Test
    public void testGetCandidates() {
        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new User());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
        
        assertEquals(expectedUsers, approvalPermissionService.getCandidates(approvalInstanceId, enabled));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        assertNotEquals(new HashMap<>(), approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(approvalInstanceId)));
    }
    
    @Test
    public void testGetCandidateRoles() {
        List<Role> expectedRoles = new ArrayList<>();
        expectedRoles.add(new Role());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());

        assertEquals(expectedRoles, approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled));
    }

    @Test
    public void testIsApprovable() {
        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertNotNull(approvalPermissionService.getApprovableApprovalInstances());
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    } 

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, "RESOURCE_ROLE_TEST");
        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        assertNotEquals(new HashMap<>(), approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L, 3L)));
    }
}
