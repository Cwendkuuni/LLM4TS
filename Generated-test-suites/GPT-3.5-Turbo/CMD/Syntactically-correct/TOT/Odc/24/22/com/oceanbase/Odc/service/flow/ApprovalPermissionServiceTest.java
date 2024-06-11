import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    private UserTaskInstanceCandidateEntity candidateEntity;

    @Before
    public void setup() {
        candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setUserId(1L);
        candidateEntity.setRoleId(1L);
        candidateEntity.setApprovalInstanceId(1L);
    }

    @Test
    public void testGetCandidates() {
        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(candidateEntity);
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(entities);

        Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, candidates.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(candidateEntity);
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(entities);

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singletonList(1L));
        assertEquals(1, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<UserTaskInstanceCandidateEntity> entities = Collections.singletonList(candidateEntity);
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(entities);

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(1L));
        assertEquals(1, result.size());
    }

    // Add more test cases for the remaining methods in ApprovalPermissionService

}