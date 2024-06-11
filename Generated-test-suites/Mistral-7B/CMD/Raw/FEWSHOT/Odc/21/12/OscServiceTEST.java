package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;
    private DefaultConnectSessionFactory connectionSessionFactory;
    private ConnectionSessionFactory connectionFactoryMock;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        connectionSessionFactory = new DefaultConnectSessionFactory(new ConnectionConfig());
        connectionFactoryMock = mock(ConnectionSessionFactory.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscService oscService = new OscService();
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);

        assertEquals(databaseId, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotFound() {
        OscService oscService = new OscService();
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(scheduleTask));

        OscService oscService = new OscService();
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenJobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule1");
        scheduleEntity.setJobType(JobType.SOME_OTHER_TYPE);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(scheduleTask));

        OscService oscService = new OscService();
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        long connectionId = 1;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("dialect1");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        ConnectionSessionFactory connectionFactory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connSession = connectionFactory.generateSession();
        String version = ConnectionSessionUtil.getVersion(connSession);

        when(connectionFactoryMock.generateSession()).thenReturn(connSession);

        OscService oscService = new OscService();
        assertTrue(oscService.getLockUserIsRequired(connectionId));
        assertSame(version, ConnectionSessionUtil.getVersion(connectionFactoryMock.generateSession()));
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionNotFound() {
        long connectionId = 1;
        ConnectionService connectionServiceMock = mock(ConnectionService.class);
        when(connectionServiceMock.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(null);

        OscService oscService = new OscService();
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionFailed() {
        long connectionId = 1;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("dialect1");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenThrow(new RuntimeException("Connection error"));

        OscService oscService = new OscService();
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}