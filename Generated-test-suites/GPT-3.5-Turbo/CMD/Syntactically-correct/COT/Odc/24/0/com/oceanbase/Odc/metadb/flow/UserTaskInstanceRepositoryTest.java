import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Test case for findByStatus method
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setStatus(FlowNodeStatus.ACTIVE);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(Collections.singletonList(userTaskInstance));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getStatus(), FlowNodeStatus.ACTIVE);
    }

    // Add test cases for other methods

}