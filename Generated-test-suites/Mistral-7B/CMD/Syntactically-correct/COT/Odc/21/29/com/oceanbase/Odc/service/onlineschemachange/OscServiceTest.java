package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

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

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = Mockito.mock(DatabaseEntity.class);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertTrue(result.getDatabaseId() == 0);
        assertFalse(result.isLockDatabaseUserRequired());

        Mockito.verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotExists() {
        Long id = 1L;

        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertTrue(result.getDatabaseId() == 0);
        assertFalse(result.isLockDatabaseUserRequired());

        Mockito.verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = Mockito.mock(OnlineSchemaChangeScheduleTaskResult.class);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Mockito.when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        Mockito.verifyNoMoreInteractions(databaseRepository);
        Mockito.verifyNoMoreInteractions(connectionService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWhenJobTypeIsNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = Mockito.mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = Mockito.mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = Mockito.mock(OnlineSchemaChangeScheduleTaskResult.class);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.SCHEDULE_TEST);

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);

        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        Mockito.verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequiredWhenConnectionIsNotValid() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = Mockito.mock(ConnectionConfig.class);

        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new RuntimeException());

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertFalse(result);

        Mockito.verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }
}