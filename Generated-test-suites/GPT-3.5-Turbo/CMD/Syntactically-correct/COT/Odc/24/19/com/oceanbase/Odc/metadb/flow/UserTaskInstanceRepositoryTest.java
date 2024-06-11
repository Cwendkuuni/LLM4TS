
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import javax.transaction.Transactional;

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
    
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Test
    public void testFindByStatus() {
        assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE));
    }
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)));
    }
    
    // Add more test cases for other methods
    
}
