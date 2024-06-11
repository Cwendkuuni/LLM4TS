
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private static ApprovalPermissionService approvalPermissionService;
    private static UserTaskInstanceCandidateRepository mockRepo;
    private static UserRepository mockUserRepo;
    private static UserService mockUserService;

    @BeforeClass
    public static void setup() {
        mockRepo = mock(UserTaskInstanceCandidateRepository.class);
        mockUserRepo = mock(UserRepository.class);
        mockUserService = mock(UserService.class);
        approvalPermissionService = new ApprovalPermissionService(mockRepo, mockUserRepo, mockUserService);
    }

    @Test
    public void testGetCandidates() {
        // Setup mock method return values
        when(mockRepo.findByApprovalInstanceId(anyLong()))
        .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity(), 
                                  new UserTaskInstanceCandidateEntity()));

        when(mockUserRepo.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
        .thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        when(mockUserRepo.findByUserIdsAndEnabled(anySet(), anyBoolean()))
        .thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        Set<User> candidates = approvalPermissionService.getCandidates(1234L, true);

        // Verify returned set size is correct
        assertEquals(4, candidates.size());
        
        // Verify that repository methods were called correctly
        verify(mockRepo, times(1)).findByApprovalInstanceId(anyLong());
        verify(mockUserRepo, times(2)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
    }
}
