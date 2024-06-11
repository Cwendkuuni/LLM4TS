
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApprovalPermissionServiceTest {

    @Mock
    private ApprovalPermissionService approvalPermissionService;
    
    @Test
    public void getCandidatesTest() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> expectedUsers = new HashSet<>(Arrays.asList(new User(1L), new User(2L)));
        when(approvalPermissionService.getCandidates(approvalInstanceId, enabled)).thenReturn(expectedUsers);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, result);
    }

    @Test
    public void getCandidatesNullTestId() {
        Long approvalInstanceId = null;
        Boolean enabled = true;
        Set<User> expectedUsers = Collections.emptySet();
        when(approvalPermissionService.getCandidates(approvalInstanceId, enabled)).thenReturn(expectedUsers);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCandidatesInvalidIdTest() {
        Long approvalInstanceId = -1L;
        Boolean enabled = true;
        approvalPermissionService.getCandidates(approvalInstanceId, enabled);
    }
}
