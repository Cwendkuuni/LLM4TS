import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertNotNull(instances);
        // Add more assertions if needed
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Test with valid userId and roleIds
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(1L, 2L));
        assertNotNull(instances);
        // Test with invalid userId and roleIds
        instances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(100L, List.of(100L, 200L));
        assertNotNull(instances);
        // Add more assertions if needed
    }

    // Add test cases for other methods

}