package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotFound_thenReturnEmptyOscDatabaseInfo() {
        // Given
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseFound_thenReturnOscDatabaseInfo() {
        // Given
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        // Then
        assertEquals(1L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenScheduleTaskNotFound_thenThrowIllegalArgumentException() {
        // Given
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_whenScheduleTaskFoundButJobTypeIsNotOnlineSchemaChange_thenThrowException() {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        // When & Then
        try {
            oscService.swapTable(1L);
            fail();
        } catch (Exception e) {
            assertEquals(ErrorCodes.BadArgument, e.getCause());
        }
    }

    @Test
    public void testSwapTable_whenLockUserIsRequiredButUserDoesNotHaveLockPrivilege_thenDoNothing() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OBSERVER);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        doThrow(new RuntimeException("test error")).when(connectionSession).close();
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(connectionService.generateSession()).thenReturn(connectionSession);
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        // When
        oscService.swapTable(1L);

        // Then
        verify(connectionSession).expire();
        verifyNoMoreInteractions(connectionSession);
    }

    @Test
    public void testSwapTable_whenLockUserIsNotRequired_thenDoNothing() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OBSERVER);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionService.generateSession()).thenReturn(connectionSession);
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        // When
        oscService.swapTable(1L);

        // Then
        verifyNoInteractions(connectionSession);
    }

    @Test
    public void testSwapTable_whenLockUserIsRequiredAndUserHasLockPrivilege_thenDoNothing() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OBSERVER);
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionService.generateSession()).thenReturn(connectionSession);
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        PreConditions preConditions = mock(PreConditions.class);
        when(preConditions.validExists(any(ResourceType.class), anyString(), anyLong(), any(Predicate.class)))
                .thenReturn(true);
        when(preConditions.validArgumentState(anyBoolean(), any(ErrorCodes.class), anyArray(), anyString()))
                .thenReturn(true);

        // When
        oscService.swapTable(1L);

        // Then
        verifyNoInteractions(connectionSession);
    }

}