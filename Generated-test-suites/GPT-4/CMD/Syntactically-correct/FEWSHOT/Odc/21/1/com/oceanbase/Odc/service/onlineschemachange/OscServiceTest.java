
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

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
    public void setUp() throws Exception {
        //set up initial condition or mock behaviors here
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity mockedDatabaseEntity = mock(DatabaseEntity.class);
        when(mockedDatabaseEntity.getDatabaseId()).thenReturn(id);
        when(mockedDatabaseEntity.getConnectionId()).thenReturn(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(mockedDatabaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity mockedScheduleTaskEntity = mock(ScheduleTaskEntity.class);
        ScheduleEntity mockedScheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters mockedOscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult mockedResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        when(mockedScheduleTaskEntity.getJobName()).thenReturn(String.valueOf(scheduleTaskId));
        when(mockedScheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(mockedOscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);
        when(mockedResult.isManualSwapTableEnabled()).thenReturn(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(mockedScheduleTaskEntity));
        when(scheduleRepository.findById(scheduleTaskId)).thenReturn(Optional.of(mockedScheduleEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig mockedConnConfig = mock(ConnectionConfig.class);
        when(mockedConnConfig.getDialectType()).thenReturn(DialectType.GENERIC);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(mockedConnConfig);
        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertTrue(result);
    }
}
