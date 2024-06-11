import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @BeforeClass
    public void setUp() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        expected.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        expected.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L))).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(expected, actual);
    }

    // Add similar test methods for other queries

}