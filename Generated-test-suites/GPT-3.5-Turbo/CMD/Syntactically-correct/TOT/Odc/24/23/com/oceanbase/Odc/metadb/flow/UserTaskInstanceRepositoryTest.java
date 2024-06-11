import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        // Setup mock data for testing
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> testList = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        // Set entity1 fields
        testList.add(entity1);

        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class))).thenReturn(testList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(testList, resultList);
    }

    // Add more test cases for other methods...

}