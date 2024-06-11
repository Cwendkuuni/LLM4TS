import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.repository.query.Param;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTaskInstanceRepositoryTest {
    
    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntityMock;
    
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(userTaskInstanceEntityMock);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(userTaskInstanceEntityMock);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L))).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        
        assertEquals(expected, actual);
    }
    
    // Add more test methods for other repository methods
    
}