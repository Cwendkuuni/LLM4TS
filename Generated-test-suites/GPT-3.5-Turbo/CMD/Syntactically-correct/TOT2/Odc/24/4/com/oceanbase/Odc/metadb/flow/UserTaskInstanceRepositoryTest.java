import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceRepository mockUserTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = mock(List.class);
        when(mockUserTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS))
                .thenReturn(userTaskInstanceEntities);

        assertEquals(userTaskInstanceEntities, userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = mock(List.class);
        when(mockUserTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                .thenReturn(userTaskInstanceEntities);

        assertEquals(userTaskInstanceEntities, userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L)));
    }

    // Add more test cases for other methods as needed

}