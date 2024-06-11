import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Test normal case
        List<UserTaskInstanceEntity> instances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);
        assertTrue(instances.size() > 0);

        // Test edge case
        instances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED);
        assertEquals(0, instances.size());

        // Test error scenario
        instances = userTaskInstanceRepository.findByStatus(null);
        assertNull(instances);
    }

    // More test methods for each repository method can be added here

}