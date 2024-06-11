
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService service;
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void setUp() {
        service = new ApprovalPermissionService();
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        service.setUserTaskCandidateRepository(userTaskCandidateRepository);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(100L);
        entity1.setRoleId(200L);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setUserId(101L);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Arrays.asList(entity1, entity2));

        Set<User> expectedUsers = new HashSet<>();
        expectedUsers.add(new User(100L));
        expectedUsers.add(new User(101L));

        Set<User> actualUsers = service.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetCandidates_emptyEntities() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());

        Set<User> actualUsers = service.getCandidates(approvalInstanceId, enabled);

        assertEquals(Collections.emptySet(), actualUsers);
    }

    // Add more test cases for other methods

}

