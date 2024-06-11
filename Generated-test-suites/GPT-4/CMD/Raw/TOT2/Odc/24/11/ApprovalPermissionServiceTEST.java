
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
//...(other imports)

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMock
    private ApprovalPermissionService service;

    @Mock
    private UserRepository mockRepo;
    
    //...(other Mocks)

    @Test
    public void getCandidates_ShouldReturnValidSet() {
        //...(set up mocks and expected results)
        
        Set<User> actual = service.getCandidates(id, enabled);
        assertEquals(expected, actual);
    }
    
    //...(Other tests follow a similar format)
}
