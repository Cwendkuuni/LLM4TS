
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;

public class OscServiceTest {
    @InjectMocks
    OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 100L;
        DatabaseEntity databasEntity = new DatabaseEntity();
        databasEntity.setDatabaseId(id);
        databasEntity.setConnectionId(123L);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databasEntity));
      
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        //Copy and modify the testStripLeadingAndTrailingQuotes() method using the SwapTable method.
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long id = 100L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(id)).thenReturn(connectionConfig);
        String version = null;
        ConnectionSession connectionSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn(version);
        
        boolean lockUserIsRequired = oscService.getLockUserIsRequired(id);
        assertNotNull(lockUserIsRequired);
    }
}
