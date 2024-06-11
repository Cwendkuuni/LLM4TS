
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import org.junit.Test;
import org.junit.Before;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    /* Define data that will be returned by mocked methods */
    private Long approvalInstanceId = 1L;
    private Set<Long> userUserId;
    private Set<Long> roleIds;
    private Set<User> expectedUsers;

    @Before
    public void setUp() {
        userUserId = new HashSet<>();
        userUserId.add(1L);
        roleIds = new HashSet<>();
        roleIds.add(1L);
        expectedUsers = new HashSet<>();
        expectedUsers.add(new User());
    }

    @Test
    public void testGetCandidates_ReturnsEnabledUsers_WhenApprovalInstanceIdIsProvidedAndFlagEnabledIsTrue() {

        /* Set the expectations for mocked methods */
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(Collections.emptyList());
        when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Collections.emptyList());

        /* Execute the method being tested */
        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

        /* Verify correctness */
        assertEquals(expectedUsers, actualUsers);
    }

}
