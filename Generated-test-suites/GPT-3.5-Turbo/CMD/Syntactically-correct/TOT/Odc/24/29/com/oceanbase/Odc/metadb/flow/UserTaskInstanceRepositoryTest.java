import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(status);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(result);
        
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(result);
        
    }

    // Other test cases for the remaining methods...

}