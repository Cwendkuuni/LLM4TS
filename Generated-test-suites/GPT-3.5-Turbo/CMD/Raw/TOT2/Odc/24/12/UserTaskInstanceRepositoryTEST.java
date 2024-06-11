import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Collections.emptyList();
        
        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        
        assertEquals(expectedList, actualList);
    }

    // Additional Test Cases for other methods will go here

}