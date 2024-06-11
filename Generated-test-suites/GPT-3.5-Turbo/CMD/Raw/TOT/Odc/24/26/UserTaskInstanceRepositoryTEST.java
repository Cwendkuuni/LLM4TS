import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        UserTaskInstanceEntity mockEntity = new UserTaskInstanceEntity();
        mockEntity.setStatus(status);

        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(Collections.singletonList(mockEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);

        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        UserTaskInstanceEntity mockEntity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(Collections.singletonList(mockEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(1, result.size());
    }

    // Add more test methods for other query methods

    // Add edge cases and error scenarios


}