package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@Transactional
@Slf4j
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

    private Log log;

    @Before
    public void setup() {
        log = mock(Log.class);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        when(databaseRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_scheduleTaskNotFound() {
        when(scheduleTaskRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(1L);
        assertNull(result);
    }

    @Test
    public void testSwapTable_scheduleTaskFound() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        when(scheduleTaskRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setResultJson("{}");
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(JsonUtils.fromJson(ArgumentMatchers.string("{}"), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result1 = new OnlineSchemaChangeScheduleTaskResult();
        result1.setManualSwapTableEnabled(true);
        result1.setManualSwapTableStarted(false);

        when(JsonUtils.fromJson(ArgumentMatchers.string("{}"), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result1);

        OscSwapTableVO result = oscService.swapTable(1L);
        assertSame(1L, result.getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_jobTypeNotOnlineSchemaChange() {
        when(scheduleRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.of(new ScheduleEntity()));

        oscService.swapTable(1L);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_swapTableTypeNotManual() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setResultJson("{}");
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(ArgumentMatchers.longValue(1L)))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);

        when(JsonUtils.fromJson(ArgumentMatchers.string("{}"), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        oscService.swapTable(1L);
    }

    @Test
    public void testGetLockUserIsRequired_connectionNotFound() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(ArgumentMatchers.longValue(1L)))
                .thenReturn(connectionConfig);

        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_connectionFound() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(ArgumentMatchers.longValue(1L)))
                .thenReturn(connectionConfig);

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connSession = mock(ConnectionSession.class);
        ConnectionSessionUtil connectionSessionUtil = mock(ConnectionSessionUtil.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn("5.5");
        when(OscDBUserUtil.isLockUserRequired(ArgumentMatchers.longValue(1), ArgumentMatchers.argument(1)))
                .thenReturn(true);

        assertTrue(oscService.getLockUserIsRequired(1L));
    }
}