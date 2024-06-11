import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepositoryImpl userTaskInstanceRepositoryImpl;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> fakeUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(fakeUserTaskInstances);

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepositoryImpl.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> fakeUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Long userId = 123L;
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(eq(userId), anyCollection())).thenReturn(fakeUserTaskInstances);

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepositoryImpl.findByCandidateUserIdOrRoleIds(userId, Collections.emptyList());

        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    // More test cases for other methods...

}