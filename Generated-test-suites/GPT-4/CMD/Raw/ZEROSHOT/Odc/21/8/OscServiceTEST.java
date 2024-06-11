
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@RunWith(MockitoJUnitRunner.class)
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
    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() throws Exception {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(anyLong())).thenReturn(database);
        assertTrue(database.isPresent());
        OscLockDatabaseUserInfo oscDatabase = new OscLockDatabaseUserInfo();
        oscDatabase.setDatabaseId(database.get().getDatabaseId());

        assertEquals(oscDatabase, oscService.getOscDatabaseInfo(id));
    }

    @Test(expected = PreConditions.ResourceNotExistsException.class)
    public void testSwapTableFailure() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableSuccess() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        Optional<ScheduleEntity> scheduleEntity = Optional.of(new ScheduleEntity());

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntity);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

}
