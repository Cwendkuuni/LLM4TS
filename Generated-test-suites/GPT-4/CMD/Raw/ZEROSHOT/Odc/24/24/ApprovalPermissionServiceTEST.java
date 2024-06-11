
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserService userService;

    private UserEntity userEntity;
    private UserTaskInstanceCandidateEntity candidateEntity;
    private Set<User> expectedUsers;

    @Before
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test User");
        userEntity.setEnabled(true);

        candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setId(1L);
        candidateEntity.setApprovalInstanceId(1L);
        candidateEntity.setUserId(userEntity.getId());

        User user = new User(userEntity);

        expectedUsers = new HashSet<>();
        expectedUsers.add(user);

        when(userTaskInstanceRepository.findByApprovalInstanceId(1L)).thenReturn(asList(candidateEntity));
        when(userRepository.findByUserIdsAndEnabled(new HashSet<>(asList(candidateEntity.getUserId())), true))
        		.thenReturn(asList(userEntity));
    }

    @Test
    public void testGetCandidates() {
        Set<User> resultUsers = approvalPermissionService.getCandidates(1L, true);
        assertEquals(expectedUsers, resultUsers);
    }

    // Generate similar tests for other methods in ApprovalPermissionService class
}

