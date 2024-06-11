
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {
    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private ApprovalPermissionService service;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Collection<User> candidates = service.getCandidates(1L, true);
        assertNotNull(candidates);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds(){
        Collection<Long> idSet = new HashSet<>();
        idSet.add(1L);
        idSet.add(2L);

        Map<Long, Set<Long>> resultMap = service.getInstanceId2CandidateRoleIds(idSet);
        assertNotNull(resultMap);
   }

   @Test
   public void testGetInstanceId2CandidateResourceRoleIdentifierIds(){
        Collection<Long> idSet = new HashSet<>();
        idSet.add(1L);
        idSet.add(2L);

        Map<Long, Set<String>> resultMap = service.getInstanceId2CandidateResourceRoleIdentifierIds(idSet);
        assertNotNull(resultMap);
   }

   @Test
   public void testGetCandidateUserIds(){
        Collection<Long> idSet = new HashSet<>();
        idSet.add(1L);
        idSet.add(2L);

        Set<Long> userIds = service.getCandidateUserIds(idSet);
        assertNotNull(userIds);
   }

   @Test
   public void testGetCandidateRoles(){
        List<Role> roles = service.getCandidateRoles(1L, true);
        assertNotNull(roles);
   }

   @Test
   public void testIsApprovable(){
        assertFalse(service.isApprovable(1L));
   }

   @Test
   public void testGetApprovableApprovalInstances(){
        List<UserTaskInstanceEntity> entities = service.getApprovableApprovalInstances();
        assertNotNull(entities);
   }

   @Test
   public void testGetCandidatesByFlowInstanceIds(){
        Collection<Long> idSet = new HashSet<>();
        idSet.add(1L);
        idSet.add(2L);

        Map<Long, Set<UserEntity>> resultMap = service.getCandidatesByFlowInstanceIds(idSet);
        assertNotNull(resultMap);
   }
}
