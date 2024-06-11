import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    // Test case for findByStatus method
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceService.findByStatus(status);

        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

    // Test case for findByCandidateUserIdOrRoleIds method
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(2L, 3L);
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

    // Add more test cases for each method as above

}