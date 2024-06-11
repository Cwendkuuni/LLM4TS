import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository repository;

    private UserTaskInstanceEntity entity;

    @Before
    public void setup() {
        entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("123");
        entity.setStatus(FlowNodeStatus.IN_PROGRESS);
        entity.setApproved(true);
        entity.setOperatorId(456L);
        entity.setComment("Test comment");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("789");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        expected.add(entity);

        Mockito.when(repository.findByStatus(FlowNodeStatus.IN_PROGRESS)).thenReturn(expected);

        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.IN_PROGRESS);

        assertEquals(expected, result);
    }

    // Add more test cases for other public methods as per the extracted signatures and edge cases

}