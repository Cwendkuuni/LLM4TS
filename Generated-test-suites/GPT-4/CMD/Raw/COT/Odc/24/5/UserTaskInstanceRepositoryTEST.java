
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    private UserTaskInstanceRepository mockRepo;
    
    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(FlowNodeStatus.SUBMITTED);
        verify(mockRepo,times(1)).findByStatus(FlowNodeStatus.SUBMITTED);
    }
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,Collections.singletonList(1L));
        verify(mockRepo,times(1)).findByCandidateUserIdOrRoleIds(1L,Collections.singletonList(1L));
    }

    //... do the same for all other methods

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrRoleIds_NullUserId() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Collections.singletonList(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrRoleIds_NullRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
    }

    //... do the same for all other methods

    //Then write specific tests for edge cases and exceptions:

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullEntity() {
        userTaskInstanceRepository.update(null);
    }

    //... do the same for all other methods
}
