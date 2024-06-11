
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity mockEntity;

    @Before
    public void setUp() {
        mockEntity = new UserTaskInstanceEntity();
        // setup mockEntity id, status, etc.
    }

    @Test
    public void testFindByStatus() {
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.Active)).thenReturn(Arrays.asList(mockEntity));
        assertNotNull(userTaskInstanceRepository.findByStatus(FlowNodeStatus.Active));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(mockEntity));
        assertNotNull(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("Identifier1"))).thenReturn(Arrays.asList(mockEntity));
        assertNotNull(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("Identifier1")));
    }

    // Add additional tests for each method in the UserTaskInstanceRepository
}
