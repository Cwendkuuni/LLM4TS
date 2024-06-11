
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

  @Mock
  private UserTaskInstanceRepository userTaskInstanceRepository;

  @InjectMocks
  private UserTaskInstanceService userTaskInstanceService;

  UserTaskInstanceEntity userTaskInstanceEntity;

  @Before
  public void setup() {
    userTaskInstanceEntity = new UserTaskInstanceEntity();
    userTaskInstanceEntity.setId(1L);
    userTaskInstanceEntity.setStatus(FlowNodeStatus.IN_PROCESS);
  }

  @Test
  public void testFindByStatus() {
    when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROCESS))
        .thenReturn(Arrays.asList(userTaskInstanceEntity));
    List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.IN_PROCESS);
    assertEquals(result.get(0).getStatus(), FlowNodeStatus.IN_PROCESS);
  }

  // Similar test cases can be written for other methods as well.
}
