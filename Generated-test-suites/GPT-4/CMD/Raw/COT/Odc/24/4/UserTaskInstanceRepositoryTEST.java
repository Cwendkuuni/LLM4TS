import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

// Depending on your testing setup there may be other imports and annotations needed

public class UserTaskInstanceRepositoryTest {
    
    // Assuming you already have a setup to get the instance of the objects for the interfaces.
    // Typically you would have a Mock object created. We will simply annotate it with @Autowired
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // When
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        
        // Then
        // Assert based on your requirement.
    }

    // Add similarly for all the methods.

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullEntity() {
        // Given
        UserTaskInstanceEntity entity = null;

        // When
        userTaskInstanceRepository.update(entity);
    }

    // Add similarly for all edge cases and exception scenarios.
}