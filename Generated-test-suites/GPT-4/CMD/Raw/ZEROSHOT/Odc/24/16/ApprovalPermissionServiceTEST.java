
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import java.util.*;

public class ApprovalPermissionServiceTest {
    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

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

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCandidatesTest() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("test user");
        userEntities.add(userEntity);
        List<UserTaskInstanceCandidateEntity> candidateEntities = new ArrayList<>();
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setUserId(1L);
        candidateEntity.setRoleId(1L);
        candidateEntities.add(candidateEntity);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(candidateEntities);
        when(userRepository.findByUserIdsAndEnabled(
                anyCollection(), anyBoolean())).thenReturn(userEntities);
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, users.size());
        assertTrue(users.contains(new User(userEntity)));
    }

    @Test
    public void getInstanceId2CandidateRoleIdsTest() {
        List<UserTaskInstanceCandidateEntity> candidateEntities = new ArrayList<>();
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntities.add(candidateEntity);
        when(userTaskCandidateRepository.findByApprovalInstanceIds(anyCollection())).thenReturn(candidateEntities);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new HashSet<>(Arrays.asList(1L)));
        assertNotNull(result);
        assertTrue(result.containsKey(candidateEntity.getApprovalInstanceId()));
        assertTrue(result.get(candidateEntity.getApprovalInstanceId()).contains(candidateEntity.getRoleId()));
    }

    @Test
    public void getInstanceId2CandidateResourceRoleIdentifierIdsTest(){
        // Implement getInstanceId2CandidateResourceRoleIdentifierIdsTest
    }

    @Test
    public void getCandidateUserIdsTest(){
        // Implement getCandidateUserIdsTest
    }

    @Test
    public void getCandidateRolesTest(){
        // Implement getCandidateRolesTest
    }

    @Test
    public void isApprovableTest(){
        // Implement isApprovableTest
    }

    @Test
    public void getApprovableApprovalInstancesTest(){
        // Implement getApprovableApprovalInstancesTest
    }

    @Test
    public void setCandidateResourceRoleTest(){
        // Implement setCandidateResourceRoleTest
    }

    @Test
    public void listApprovableExternalInstancesTest(){
        // Implement listApprovableExternalInstancesTest
    }

    @Test
    public void getCandidatesByFlowInstanceIds(){
        // Implement getCandidatesByFlowInstanceIdsTest
    }
}
