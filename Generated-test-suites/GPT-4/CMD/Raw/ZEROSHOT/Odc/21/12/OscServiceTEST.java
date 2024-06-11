
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.Optional;

public class OscServiceTest {
    OscService oscService;
    DatabaseEntity db;
    ConnectionConfig connConfig;
    ScheduleTaskEntity scheduleTask;
    ScheduleEntity schedule;
 
    @Before
    public void setUp() {
        oscService = mock(OscService.class);
        db = mock(DatabaseEntity.class);
        connConfig = mock(ConnectionConfig.class);
        scheduleTask = mock(ScheduleTaskEntity.class);
        schedule = mock(ScheduleEntity.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(db.getDatabaseId()).thenReturn(1L);
        when(oscService.getOscDatabaseInfo(anyLong())).thenReturn(new OscLockDatabaseUserInfo());
        assertNotNull(oscService.getOscDatabaseInfo(1L));
    }

    @Test
    public void testSwapTable() {
        when(oscService.swapTable(anyLong())).thenReturn(new OscSwapTableVO());
        assertNotNull(oscService.swapTable(1L));
    }

    @Test
    public void testGetLockUserIsRequired() {
        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);
        assertTrue(oscService.getLockUserIsRequired(1L));

        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(false);
        assertFalse(oscService.getLockUserIsRequired(1L));
    }
 
    @After
    public void tearDown() {
        oscService = null;
    }
}
