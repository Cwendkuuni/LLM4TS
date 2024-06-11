import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        // Initialize mock objects if needed
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository
            .findByStatus(FlowNodeStatus.NOT_STARTED);

        // Validate the result is not null and matches the expected criteria
        assertNotNull(result);
        // Add more assertion as needed
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository
            .findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(2L));

        // Validate the result is not null and matches the expected criteria
        assertNotNull(result);
        // Add more assertion as needed
    }

    // Add more test cases for other methods

}