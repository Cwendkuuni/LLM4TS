import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.UserService;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {

    @Test
    public void testGetCandidates() {
        // Setup
        ApprovalPermissionService service = new ApprovalPermissionService();
        UserTaskInstanceCandidateRepository mockRepository = mock(UserTaskInstanceCandidateRepository.class);
        service.setUserTaskCandidateRepository(mockRepository);
        Long approvalInstanceId = 123L;
        Boolean enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        when(mockRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        // Test
        Set<User> result = service.getCandidates(approvalInstanceId, enabled);

        // Verify
        assertEquals(new HashSet<User>(), result);
    }

    // Add more test methods for other public methods in ApprovalPermissionService

    @Test
    public void testIsApprovable() {
        // Setup
        ApprovalPermissionService service = new ApprovalPermissionService();
        Long approvalInstanceId = 123L;
        Set<Long> approvableIds = new HashSet<>(Arrays.asList(123L, 456L));
        ApprovalPermissionService spyService = spy(service);
        doReturn(approvableIds).when(spyService).getApprovableApprovalInstances();

        // Test
        boolean result = spyService.isApprovable(approvalInstanceId);

        // Verify
        assertEquals(true, result);
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Setup
        ApprovalPermissionService service = new ApprovalPermissionService();
        UserTaskInstanceRepository mockRepository = mock(UserTaskInstanceRepository.class);
        service.setUserTaskInstanceRepository(mockRepository);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setExternalApprovalId(123L);
        entity1.setExternalFlowInstanceId(456L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setExternalApprovalId(null);
        entity2.setExternalFlowInstanceId(null);
        when(mockRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Arrays.asList(entity1, entity2));

        // Test
        List<UserTaskInstanceEntity> result = service.listApprovableExternalInstances();

        // Verify
        assertEquals(1, result.size());
        assertEquals(123L, result.get(0).getExternalApprovalId().longValue());
        assertEquals(456L, result.get(0).getExternalFlowInstanceId().longValue());
    }

    // Add additional test methods as needed

}