import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private UserTaskInstanceEntity entity;
    private List<UserTaskInstanceEntity> entityList;

    @Before
    public void setup() {
        entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId1");
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("Comment");
        entity.setExpireIntervalSeconds(3600);
        entity.setExternalFlowInstanceId("externalFlowInstanceId");

        entityList = new ArrayList<>();
        entityList.add(entity);
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(entityList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryImpl.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
    }

    // Add more test methods for other repository methods

}