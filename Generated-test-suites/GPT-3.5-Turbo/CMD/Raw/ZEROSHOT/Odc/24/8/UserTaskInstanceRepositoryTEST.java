import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;
        
        when(userTaskInstanceRepositoryMock.findByStatus(status)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);
        
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    // Add more test methods for other repository methods
    
}