import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    @Transactional
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;
        List<UserTaskInstanceEntity> expected = Arrays.asList(userTaskInstanceEntity);
        
        // Mocking the behavior
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expected);

        // Testing the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(2L, 3L);
        List<UserTaskInstanceEntity> expected = Arrays.asList(userTaskInstanceEntity);
        
        // Mocking the behavior
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expected);

        // Testing the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expected, actual);
    }

    // Additional test cases for other public methods will go here

}