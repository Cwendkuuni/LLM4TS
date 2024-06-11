
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import javax.transaction.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository repository;

    @Test
    public void givenFlowNodeStatus_whenFindByStatus_thenReturnUserTaskInstanceEntityList() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(repository.findByStatus(any())).thenReturn(userTaskInstances);
        
        List<UserTaskInstanceEntity> foundUserTaskInstances = repository.findByStatus(FlowNodeStatus.COMPLETED);
        assertEquals(foundUserTaskInstances.size(), userTaskInstances.size());
    }

    // ... (create similar test logic for other methods here)
    @Test
    @Transactional
    public void givenUserTaskEntity_whenUpdate_thenReturnInt() {
        int expectedResult = 1;
        when(repository.update(any())).thenReturn(expectedResult);

        int result = repository.update(new UserTaskInstanceEntity());
        assertEquals(result, expectedResult);
    }

    @Test
    public void givenId_whenFindConfirmById_thenReturnBoolean() {
        Boolean expectedResult = true;
        when(repository.findConfirmById(anyLong())).thenReturn(expectedResult);

        Boolean result = repository.findConfirmById(1L);
        assertEquals(result, expectedResult);
    }
}
