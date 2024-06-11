import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceParams;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceUpdateParams;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    private List<UserTaskInstanceEntity> userTaskInstances;

    @Before
    public void setup() {
        userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(userTaskInstances, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertEquals(userTaskInstances, result);
    }

    // Add more test methods for the remaining queries

}