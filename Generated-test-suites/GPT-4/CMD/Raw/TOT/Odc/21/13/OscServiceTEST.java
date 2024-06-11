import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

// Assuming the existence of these classes
import com.oceanbase.odc.metadb.connection.DatabaseEntity;


public class OscServiceTest {
   
    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;


    @BeforeClass
    public static void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetOscDatabaseInfo(){
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        // etc.
    }

    // Similarly, other test cases can go here...
}