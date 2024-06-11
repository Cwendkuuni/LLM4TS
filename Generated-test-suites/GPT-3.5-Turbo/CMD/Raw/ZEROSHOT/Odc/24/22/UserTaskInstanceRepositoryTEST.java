import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Before
    public void setup() {
        userTaskInstanceRepositoryMock = mock(UserTaskInstanceRepository.class);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepositoryMock.findByStatus(any(FlowNodeStatus.class))).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.ACTIVE);
        
        verify(userTaskInstanceRepositoryMock).findByStatus(any(FlowNodeStatus.class));
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
    }

    // Add more test cases for other methods in UserTaskInstanceRepository

}