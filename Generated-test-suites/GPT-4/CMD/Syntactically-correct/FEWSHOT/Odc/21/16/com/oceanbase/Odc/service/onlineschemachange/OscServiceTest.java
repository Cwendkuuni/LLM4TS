package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Optional;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Before
    public void setup() {
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(any(Long.class))).thenReturn(new ConnectionConfig());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
        assertTrue(result.getDatabaseId() != null);
    }

    @Test
    public void testGetOscDatabaseInfoDatabaseNotFound() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
        assertNull(result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleEntity()));
        OscSwapTableVO result = oscService.swapTable(1L);
        assertNotNull(result);
        assertTrue(result.getScheduleTaskId() != null);
    }
}