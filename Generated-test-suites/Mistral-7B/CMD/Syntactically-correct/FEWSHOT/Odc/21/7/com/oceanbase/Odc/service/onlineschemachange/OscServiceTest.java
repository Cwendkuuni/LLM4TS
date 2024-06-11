package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.service.onlineschemachange.OscService.ErrorCodes.BadArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
import com.oceanbase.odc.service.connection.model.ConnectionConfigDto;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@Component
public class OscServiceTest {

    @Autowired
    private DatabaseRepository databaseRepository;
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private OscService oscService;

    private DatabaseEntity dbEntity;
    private ScheduleEntity scheduleEntity;
    private ConnectionConfig connectionConfig;
    private ConnectionSessionFactory connectionSessionFactory;
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        dbEntity = new DatabaseEntity();
        dbEntity.setDatabaseId(1L);
        dbEntity.setConnectionId(1L);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setDialectVersion("8.0.22");
        connectionConfig.setUrl("jdbc:mysql://localhost:3306");
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");

        connectionSessionFactory = new DefaultConnectSessionFactory(connectionConfig);
        connectionSession = connectionSessionFactory.generateSession();
    }

    @Test
    public void testGetOscDatabaseInfo_NonexistentDatabase() {
        Optional<DatabaseEntity> optionalDbEntity = Optional.ofNullable(null);
        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();

        when(databaseRepository.findById(dbEntity.getDatabaseId())).thenReturn(optionalDbEntity);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(dbEntity.getDatabaseId());
        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_ExistentDatabase() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(databaseRepository.findById(dbEntity.getDatabaseId())).thenReturn(Optional.of(dbEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(dbEntity.getDatabaseId());
        assertEquals(dbEntity.getDatabaseId(), result.getDatabaseId());
        assertTrue(getLockUserIsRequired(dbEntity.getConnectionId()));
        assertEquals(result.getLockDatabaseUserRequired(), getLockUserIsRequired(dbEntity.getConnectionId()));
    }

    @Test
    public void testSwapTable_NonexistentScheduleTask() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscParameters.setDatabaseId(dbEntity.getDatabaseId());

        when(databaseRepository.findById(dbEntity.getDatabaseId())).thenReturn(Optional.of(dbEntity));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(null));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test
    public void testSwapTable_InvalidScheduleTask() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscParameters.setDatabaseId(dbEntity.getDatabaseId());

        when(databaseRepository.findById(dbEntity.getDatabaseId())).thenReturn(Optional.of(dbEntity));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(null));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
    }

    @Test
    public void testSwapTable_ValidScheduleTask() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscParameters.setDatabaseId(dbEntity.getDatabaseId());

        when(databaseRepository.findById(dbEntity.getDatabaseId())).thenReturn(Optional.of(dbEntity));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertSame(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_NoLockUserRequired() {
        assertFalse(getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_LockUserRequired() {
        assertTrue(getLockUserIsRequired(2L));
    }

    private boolean getLockUserIsRequired(Long connectionId) {
        return oscService.getLockUserIsRequired(connectionId);
    }
}