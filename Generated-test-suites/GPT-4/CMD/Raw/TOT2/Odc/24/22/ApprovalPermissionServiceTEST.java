import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approverPermissionService;

    @Mock
    private User userMock;

    @Mock
    private UserService userServiceMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(userMock);
        when(userServiceMock.findByApprovalInstanceIdAndEnabled(1L, true)).thenReturn(expectedUsers);
        Set<User> actualUsers = approverPermissionService.getCandidates(1L, true);
        assertTrue(actualUsers.equals(expectedUsers));
    }

    @Test
    public void testIsApprovable() {
        when(userServiceMock.getApprovableApprovalInstances()).thenReturn(new HashSet<>());
        Boolean approvable = approverPermissionService.isApprovable(1L);
        assertFalse(approvable);
    }
}