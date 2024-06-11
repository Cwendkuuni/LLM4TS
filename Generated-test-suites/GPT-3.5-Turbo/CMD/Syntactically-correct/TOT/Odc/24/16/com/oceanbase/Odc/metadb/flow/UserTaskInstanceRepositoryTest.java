
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepositoryImpl userTaskInstanceRepositoryImpl;

    private UserTaskInstanceEntity entity;

    @Before
    public void setup() {
        entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("task123");
        entity.setStatus(FlowNodeStatus.PENDING);
    }

    @Test
    public void testFindByStatus() {
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(Arrays.asList(entity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("task123", result.get(0).getUserTaskId());
    }

    // Add more test methods for other repository methods

}

