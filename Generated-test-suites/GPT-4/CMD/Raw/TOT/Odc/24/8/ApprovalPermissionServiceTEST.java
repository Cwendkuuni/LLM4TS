import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;

    @MockBean
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Test
    public void getCandidatesTest() {
        // Arrange
        Long approvalInstanceId = 1L;
        Boolean isEnabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(Collections.emptyList());

        // Act
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, isEnabled);

        // Assert
        assertTrue(result.isEmpty());
    }

    // Similarly, we can write test methods for other methods.

}