
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.User;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        UserTaskInstanceCandidateEntity entity1 =
                new UserTaskInstanceCandidateEntity(); // assume there exists a constructor or setters
        entity1.setApprovalInstanceId(1L);
        entity1.setUserId(1L);
        entity1.setRoleId(1L);

        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Arrays.asList(entity1));

        Set<User> customers = approvalPermissionService.getCandidates(1L, true);

        assertEquals(1, customers.size());
        // Assert other fields as necessary   
    }
}
