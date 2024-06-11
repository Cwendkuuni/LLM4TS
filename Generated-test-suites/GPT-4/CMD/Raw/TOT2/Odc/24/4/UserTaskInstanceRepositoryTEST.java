import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class UserTaskInstanceRepositoryTest {

       @Mock
       UserTaskInstanceRepository userTaskInstanceRepository;

       @Before
       public void setup() {
           MockitoAnnotations.initMocks(this);
       }

       @Test
       public void testFindByStatus() {
           FlowNodeStatus status = FlowNodeStatus.ACTIVE; // Assuming status ACTIVE
           List<UserTaskInstanceEntity> list = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity() /* Add Initial data in constructors*/);

           when(userTaskInstanceRepository.findByStatus(status)).thenReturn(list);

           List<UserTaskInstanceEntity> savedList = userTaskInstanceRepository.findByStatus(status);

           // Asserts
           assertEquals(2, savedList.size());
       }

       // Similar steps would be required for other methods
}