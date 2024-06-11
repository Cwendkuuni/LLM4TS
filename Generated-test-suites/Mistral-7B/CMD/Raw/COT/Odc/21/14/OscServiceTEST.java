package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;
    private DefaultConnectSessionFactory sessionFactory;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = Mockito.mock(DatabaseRepository.class);
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        sessionFactory = Mockito.mock(DefaultConnectSessionFactory.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setConnectionService(connectionService);
        oscService.setSessionFactory(sessionFactory);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> dbEntityOptional = Optional.of(new DatabaseEntity());
        Mockito.when(databaseRepository.findById(id)).thenReturn(dbEntityOptional);

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabaseInfo instanceof OscLockDatabaseUserInfo);
        assertEquals(0L, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());

        DatabaseEntity newDbEntity = new DatabaseEntity();
        newDbEntity.setConnectionId(2L);
        Optional<DatabaseEntity> newDbEntityOptional = Optional.of(newDbEntity);
        Mockito.when(databaseRepository.findById(id)).thenReturn(newDbEntityOptional);
        assertTrue(oscService.getLockUserIsRequired(id));
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("scheduleId");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(scheduleEntityOptional);

        ScheduleTaskEntity newScheduleTaskEntity = new ScheduleTaskEntity();
        newScheduleTaskEntity.setId(scheduleTaskId);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(newScheduleTaskEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        Mockito.when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result)))
                .answer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithInvalidScheduleTask() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("scheduleId");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Mockito.when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(scheduleEntityOptional);

        ScheduleTaskEntity newScheduleTaskEntity = new ScheduleTaskEntity();
        newScheduleTaskEntity.setId(scheduleTaskId);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("dialectType");
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        String version = Mockito.mock(String.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(sessionFactory.generateSession()).thenReturn(connSession);
        Mockito.when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        ConnectionConfig newConnectionConfig = new ConnectionConfig();
        newConnectionConfig.setDialectType("anotherDialectType");
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(newConnectionConfig);

        Mockito.doReturn("version_1").when(ConnectionSessionUtil).getVersion(connSession);

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }
}