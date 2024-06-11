package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("deprecation")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;
    private OscService oscService;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.scheduleRepository = scheduleRepository;
                this.scheduleTaskRepository = scheduleTaskRepository;
                this.connectionService = connectionService;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithNullScheduleTaskId() {
        oscService.swapTable(null);
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(1L);
        });

        assertEquals("Schedule task not found", exception.getMessage());
    }

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));

        oscService.swapTable(1L);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, Mockito.anyString());

        OnlineSchemaChangeScheduleTaskResult newResult = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);

        assertFalse(newResult.isManualSwapTableEnabled());
        assertTrue(newResult.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequiredWithNotLockUserRequiredConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequiredWithLockUserRequiredConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(ConnectionSessionUtil.getVersion(Mockito.any())).thenReturn("5.0.0");
        assertTrue(oscService.getLockUserIsRequired(1L));
    }
}