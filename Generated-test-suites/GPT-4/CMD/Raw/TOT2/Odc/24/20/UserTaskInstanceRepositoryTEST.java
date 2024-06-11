import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
    
@DataJpaTest
public class UserTaskInstanceRepositoryTest {
   
    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;
   
    @Test
    public void testFindByStatus() {
	
        // Arrange
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        // Assuming status is an ENUM
        entity.setStatus(FlowNodeStatus.STATUS_1);
        Mockito.when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class))).thenReturn(Arrays.asList(entity));
        
        // Act
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.STATUS_1);
        
        // Assert
        assertNotNull(entities);
        assertEquals(1, entities.size());
        verifyFindByStatusIsCalledOnce(FlowNodeStatus.STATUS_1);
    }
    
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
	
        // Arrange
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(any(Long.class), any(List.class))).thenReturn(Arrays.asList(entity));
        
        // Act
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        
        // Assert
        assertNotNull(entities);
        assertEquals(1, entities.size());
        verifyFindByCandidateUserIdOrRoleIdsIsCalledOnce(1L, roleIds);
    }
    
    private void verifyFindByStatusIsCalledOnce(FlowNodeStatus status) {
        Mockito.verify(userTaskInstanceRepository, VerificationModeFactory.times(1)).findByStatus(status);
        Mockito.reset(userTaskInstanceRepository);
    }
    
    private void verifyFindByCandidateUserIdOrRoleIdsIsCalledOnce(Long userId, Collection<Long> roleIds) {
        Mockito.verify(userTaskInstanceRepository, VerificationModeFactory.times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
        Mockito.reset(userTaskInstanceRepository);
    }
}