import org.junit.*;
import org.junit.runner.RunWith;

// Import necessary packages

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Test findByStatus method
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        Assert.assertNotNull(result);
        // Add more assertions based on the expected behavior
    }

    // Add more test methods for other public methods

    // Edge case: empty roleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIds_EmptyRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(123L, Collections.emptyList());
        Assert.assertNotNull(result);
        // Add assertions for the edge case scenario
    }

    // Exception handling: Non-existent roleId
    @Test
    public void testFindByRoleId_NonExistentRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(999L);
        Assert.assertTrue(result.isEmpty());
        // Add assertions to handle the exception scenario
    }

    // Merge all test cases into a complete unit test file

}