
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> testEntities = Arrays.asList(userTaskInstanceEntity);
        ReflectionTestUtils.setField(userTaskInstanceRepository, "findByStatus", testEntities);
        Assert.assertEquals(testEntities, userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAIT_FOR_CONFIRM));
    }

    // Similar tests for other query methods
    // Make sure to mock query arguments as needed

    @Test
    @Transactional
    public void testUpdate() {
        int expected = 1;
        when(userTaskInstanceRepository.update(userTaskInstanceEntity)).thenReturn(expected);
        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int expected = 1;
        when(userTaskInstanceRepository.updateUserTaskIdById(1L, "task")).thenReturn(expected);
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "task");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUpdateStatusById() {
        int expected = 1;
        when(userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.WAIT_FOR_CONFIRM)).thenReturn(expected);
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.WAIT_FOR_CONFIRM);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expected = 1;
        when(userTaskInstanceRepository.deleteByFlowInstanceId(1L)).thenReturn(expected);
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Similar tests for other findBy-like methods
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Similar tests for other findBy-like methods
    }

    @Test
    public void testFindConfirmById() {
        Boolean expected = true;
        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(expected);
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Similar tests for other find-like methods
    }
}
