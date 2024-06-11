
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@RunWith(SpringRunner.class)
public class UserTaskInstanceRepositoryTest {
	
	@InjectMocks
	UserTaskInstanceRepository userTaskInstanceRepository;
	
	@Mock
	UserTaskInstanceRepository mockUserTaskInstanceRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFindByStatus() {
		List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
		UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
		entity.setStatus(FlowNodeStatus.APPROVED);
		expectedList.add(entity);
		
		when(mockUserTaskInstanceRepository.findByStatus(eq(FlowNodeStatus.APPROVED))).thenReturn(expectedList);
		
		List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
		
		assertNotNull(actualList);
		assertEquals(1, actualList.size());
		assertEquals(FlowNodeStatus.APPROVED, actualList.get(0).getStatus());
	}
	
	// Add more test cases for other methods
	
}
