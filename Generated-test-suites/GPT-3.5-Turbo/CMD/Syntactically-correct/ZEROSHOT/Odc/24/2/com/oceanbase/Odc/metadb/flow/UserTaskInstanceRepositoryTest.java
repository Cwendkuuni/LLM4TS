import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryImpl;

    @Before
    public void setup() {
        userTaskInstanceRepositoryImpl = new UserTaskInstanceRepositoryImpl();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryImpl.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(userTaskInstanceEntities.size(), result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryImpl.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(userTaskInstanceEntities.size(), result.size());
    }

    // Add more test methods for other repository methods

}