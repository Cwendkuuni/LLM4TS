
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntityMock;

    @Before
    public void setup() {
        userTaskInstanceEntityMock = Mockito.mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceEntityMock.getId()).thenReturn(1L);
    }

    @Test
    public void findByStatusTest() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ON_HOLD))
            .thenReturn(Arrays.asList(userTaskInstanceEntityMock));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ON_HOLD);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntityMock.getId(), result.get(0).getId());
    }

    @Test
    public void updateTest() {
        when(userTaskInstanceRepository.update(userTaskInstanceEntityMock)).thenReturn(1);
        int result = userTaskInstanceRepository.update(userTaskInstanceEntityMock);
        assertEquals(1, result);
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(userTaskInstanceRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void findByInstanceTypeAndActivityIdTest() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId1", 1L))
            .thenReturn(Optional.of(userTaskInstanceEntityMock));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId1", 1L);
        assertEquals(userTaskInstanceEntityMock.getId(), result.get().getId());
    }
}
