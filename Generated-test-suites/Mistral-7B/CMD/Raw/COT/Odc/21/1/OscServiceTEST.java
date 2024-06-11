package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.session.utils.ConnectionSessionUtil;

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

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_withNullId_returnsEmptyOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(0L);

        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_withValidId_returnsOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);

        assertThat(result).isNotNull();
        assertThat(result.getDatabaseId()).isEqualTo(databaseId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_withNullScheduleTaskId_throwsIllegalArgumentException() {
        oscService.swapTable(null);
    }

    @Test
    public void testSwapTable_withValidScheduleTaskId_callsDatabaseRepositoryFindByIdAndUpdateTaskResult() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(eq(scheduleEntity.getDatabaseId()))).thenReturn(Optional.of(new DatabaseEntity()));

        oscService.swapTable(scheduleTaskId);

        verify(databaseRepository).findById(eq(scheduleEntity.getDatabaseId()));
        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_withNonManualSwapTable_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(eq(scheduleEntity.getDatabaseId()))).thenReturn(Optional.of(new DatabaseEntity()));
        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(false);

        assertFalse(oscService.getLockUserIsRequired(scheduleEntity.getConnectionId()));

        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.AUTOMATIC);
        when(result.isManualSwapTableEnabled()).thenReturn(false);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_withManualSwapTable_callsDatabaseRepositoryFindByIdAndUpdateTaskResult() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(eq(scheduleEntity.getDatabaseId()))).thenReturn(Optional.of(new DatabaseEntity()));
        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);

        oscService.swapTable(scheduleTaskId);

        verify(databaseRepository).findById(eq(scheduleEntity.getDatabaseId()));
        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test
    public void testGetLockUserIsRequired_withNonLockUser_returnsFalse() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        String version = "5.3.3";

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_withLockUser_returnsTrue() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        String version = null;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }
}