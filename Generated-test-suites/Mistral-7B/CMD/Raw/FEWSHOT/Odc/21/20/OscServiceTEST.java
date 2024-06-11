package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.PreConditions.validExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

/**
 * @author yaobin
 */
@Slf4j
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
    private DefaultConnectSessionFactory connectionSessionFactory;

    @Mock
    private ConnectionSessionUtil connectionSessionUtil;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(connectionSessionFactory.generateSession()).thenReturn(mock(ConnectionSession.class));
        when(oscDBUserUtil.isLockUserRequired(any(DialectType.class), any()))
                .thenReturn(false);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity().setDatabaseId(databaseId));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist() {
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity().setJobName("scheduleId");
        ScheduleEntity scheduleEntity = new ScheduleEntity().setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters().setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult().setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, eq(JsonUtils.toJson(resultCaptor.capture())));

        OnlineSchemaChangeScheduleTaskResult capturedResult = resultCaptor.getValue();
        assertTrue(capturedResult.isManualSwapTableEnabled());
        assertFalse(capturedResult.isManualSwapTableStarted());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_whenTaskTypeIsNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity().setJobName("scheduleId").setJobType(JobType.HELP);
        ScheduleEntity scheduleEntity = new ScheduleEntity().setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        boolean expectedResult = false;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertEquals(expectedResult, result);
    }
}