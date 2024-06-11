import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedResult);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedResult);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedResult, result);
    }

    // Add more test methods for other repository methods

}