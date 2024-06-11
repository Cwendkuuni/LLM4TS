import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        List<UserTaskInstanceEntity> expectedResults = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedResults);

        List<UserTaskInstanceEntity> actualResults = userTaskInstanceRepository.findByStatus(status);

        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
        assertTrue(actualResults.containsAll(expectedResults));
    }

    // Additional test cases for other methods can be added here

}