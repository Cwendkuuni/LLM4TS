import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

    private List<UserTaskInstanceEntity> userTaskInstanceEntities;

    @Before
    public void setup() {
        userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(userTaskInstanceEntities);

        assertEquals(userTaskInstanceEntities, userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE));
    }

    // Add test cases for other methods...

}