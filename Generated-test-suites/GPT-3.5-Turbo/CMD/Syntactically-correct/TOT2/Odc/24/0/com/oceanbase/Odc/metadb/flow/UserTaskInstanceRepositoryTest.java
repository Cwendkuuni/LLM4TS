import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> resultList = new ArrayList<>();
        Mockito.when(userRepository.findByStatus(FlowNodeStatus.ACTIVE)).thenReturn(resultList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);

        assertEquals(resultList, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> resultList = new ArrayList<>();
        Mockito.when(
                userRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.anyCollection())).thenReturn(resultList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());

        assertEquals(resultList, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> resultList = new ArrayList<>();
        Mockito.when(
                userRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.anyLong(), Mockito.anyCollection())).thenReturn(resultList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());

        assertEquals(resultList, result);
    }

    // Continue testing for other methods following the same pattern

}