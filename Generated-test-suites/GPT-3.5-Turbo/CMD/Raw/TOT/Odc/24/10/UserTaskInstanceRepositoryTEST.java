import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setStatus(status);
        List<UserTaskInstanceEntity> entities = Collections.singletonList(userTaskInstanceEntity);
        
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(entities);
        
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(status);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status, result.get(0).getStatus());
    }

    // Add test cases for other methods

    // Edge cases, typical use cases, and error scenarios can be tested similarly

}