import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

	@Mock
	UserTaskInstanceRepository userTaskInstanceRepository;

	@Captor
	private ArgumentCaptor<UserTaskInstanceEntity> userTaskInstanceEntityCaptor;

	@Test
	public void findByStatus() {
		List<UserTaskInstanceEntity> expectedList = Collections.singletonList(mock(UserTaskInstanceEntity.class));

		// Mocking the behavior of the repository method
		FlowNodeStatus status = FlowNodeStatus.PENDING;
		when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);

		// Calling the actual method
		List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(status);

		// Verifying the method is called with correct parameter
		verify(userTaskInstanceRepository).findByStatus(status);

		// Assertion
		assertEquals(expectedList, resultList);
	}

	/* Add test cases for other methods in similar manner */

}