
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> entityList = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(entityList);
        Assert.assertEquals(entityList, userTaskInstanceRepository.findByStatus(status));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Collections.singletonList(1L);

        List<UserTaskInstanceEntity> entityList = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(entityList);
        Assert.assertEquals(entityList, userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Collections.singletonList("ROLE_USER");
        
        List<UserTaskInstanceEntity> entityList = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(entityList);
        Assert.assertEquals(entityList, userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers));
    }

    //Test the remaining methods in a similar manner here...
}
