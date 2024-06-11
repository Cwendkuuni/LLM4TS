import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;

public class OscServiceTest {
    
    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOscDatabaseInfoTest() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = new OscLockDatabaseUserInfo();
        oscLockDatabaseUserInfo.setDatabaseId(id);
        Optional<DatabaseEntity> optionalEntity = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(optionalEntity);
        OscLockDatabaseUserInfo receivedOscDatabase = oscService.getOscDatabaseInfo(id);
        assertNotNull(receivedOscDatabase);
        assertEquals(oscLockDatabaseUserInfo.getDatabaseId(), receivedOscDatabase.getDatabaseId());
    }

    @Test
    public void swapTableTest() {
        Long id = 1L;
        OscSwapTableVO oscSwapTableVO = new OscSwapTableVO();
        oscSwapTableVO.setScheduleTaskId(id);
        OscSwapTableVO receivedOscSwapTable = oscService.swapTable(id);
        assertNotNull(receivedOscSwapTable);
        assertEquals(oscSwapTableVO.getScheduleTaskId(), receivedOscSwapTable.getScheduleTaskId());
    }
}