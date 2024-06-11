
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ConnectionService connectionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);
        assertNotNull(result);
        assertEquals(Long.valueOf(123), result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscSwapTableVO result = oscService.swapTable(123L);
        assertNotNull(result);
        assertEquals(Long.valueOf(123), result.getScheduleTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        oscService.swapTable(123L);
    }
}
