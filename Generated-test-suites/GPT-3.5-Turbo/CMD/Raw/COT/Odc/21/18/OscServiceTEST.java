
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;

public class OscServiceTest {

    private OscService oscService;

    private DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
    private ConnectionService connectionService = mock(ConnectionService.class);

    @Before
    public void setUp() {
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(java.util.Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        // Add more assertions for the expected values in OscLockDatabaseUserInfo
    }

    @Test
    public void testSwapTable() {
        // Write test case for swapTable method
    }

    // Add more test cases for other methods and edge cases

}
