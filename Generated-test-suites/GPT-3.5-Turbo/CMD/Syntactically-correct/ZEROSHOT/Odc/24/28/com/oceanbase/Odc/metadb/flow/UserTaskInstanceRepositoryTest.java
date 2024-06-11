import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.NEW;
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(Collections.emptyList());
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(status);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Collections.singletonList(2L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(Collections.emptyList());
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Add more test methods for other repository methods

}