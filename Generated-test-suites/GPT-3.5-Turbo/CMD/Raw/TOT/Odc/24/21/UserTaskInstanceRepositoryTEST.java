import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(new UserTaskInstanceEntity());
        
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L))).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        
        assertEquals(expected, actual);
    }

    // Add more test cases for other methods...

}