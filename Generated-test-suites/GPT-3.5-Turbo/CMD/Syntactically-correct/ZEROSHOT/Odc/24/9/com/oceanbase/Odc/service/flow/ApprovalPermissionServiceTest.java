import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    private UserTaskInstanceCandidateEntity candidateEntity1;
    private UserTaskInstanceCandidateEntity candidateEntity2;

    @Before
    public void setUp() {
        candidateEntity1 = new UserTaskInstanceCandidateEntity();
        candidateEntity1.setUserId(1L);
        candidateEntity1.setRoleId(2L);

        candidateEntity2 = new UserTaskInstanceCandidateEntity();
        candidateEntity2.setUserId(3L);
        candidateEntity2.setRoleId(4L);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 123L;
        Boolean enabled = true;

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(candidateEntity1);
        entities.add(candidateEntity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new User(1L));
        expectedUsers.add(new User(3L));

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(123L, 456L);
        
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(candidateEntity1);
        entities.add(candidateEntity2);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<Long>> expectedMap = new HashMap<>();
        Set<Long> roleIds1 = new HashSet<>(Arrays.asList(2L, 4L));
        expectedMap.put(123L, roleIds1);
        expectedMap.put(456L, roleIds1);

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(expectedMap, actualMap);
    }

    // Add more test methods for other public methods in ApprovalPermissionService class

}