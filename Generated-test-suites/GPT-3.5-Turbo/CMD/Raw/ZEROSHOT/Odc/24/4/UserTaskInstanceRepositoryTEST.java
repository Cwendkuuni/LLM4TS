import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepo;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("UT-001");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.ACTIVE);
        userTaskInstanceEntity.setApproved(true);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepo.findByStatus(FlowNodeStatus.ACTIVE);

        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L))).thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepo.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
    }

    // Add more test cases for other methods in the UserTaskInstanceRepository class

}