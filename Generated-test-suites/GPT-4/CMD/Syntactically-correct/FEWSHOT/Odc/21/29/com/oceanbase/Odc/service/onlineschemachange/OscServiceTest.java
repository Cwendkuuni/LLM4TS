
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.util.Optional;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OscServiceTest {
    
    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);
        Optional<DatabaseEntity> optionalDatabase = Optional.of(databaseEntity);
        when(databaseRepository.findById(id)).thenReturn(optionalDatabase);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(1L, result.getDatabaseId().longValue());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        Optional<ScheduleTaskEntity> optionalScheduleTask = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(optionalScheduleTask);
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
    }
}
