package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Autowired
    private DatabaseRepository databaseRepository;
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private OscService oscService;
    private DatabaseEntity databaseEntity;
    private ConnectionConfig connectionConfig;
    private ConnectionSessionFactory connectionSessionFactory;
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);

        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        connectionSessionFactory = new DefaultConnectSessionFactory(connectionConfig);
        connectionSession = mock(ConnectionSession.class);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithEmptyDatabase() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

        assertNull(oscDatabaseInfo);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("schedule-1");
        scheduleTask.setJobParametersJson("{}");
        scheduleTask.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = JsonUtils.fromJson(scheduleEntity.getJobParametersJson(),
                OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("schedule-1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        PreConditions.expectPreconditionMet(ErrorCodes.BadArgument,
                () -> oscService.getLockUserIsRequired(1L));

        ConnectionSession connectionSession = oscService.getForConnectionSkipPermissionCheck(1L);
        String version = "5.5.55";
        ConnectionSessionUtil.setVersion(connectionSession, version);

        assertTrue(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> version));
    }

    @Test
    public void testGetLockUserIsRequiredWithNoVersion() {
        PreConditions.expectPreconditionMet(ErrorCodes.BadArgument,
                () -> oscService.getLockUserIsRequired(1L));

        oscService.getForConnectionSkipPermissionCheck(1L);
    }

    @Test
    public void testGetLockUserIsRequiredWithNoLockUserRequired() {
        ConnectionSession connectionSession = oscService.getForConnectionSkipPermissionCheck(1L);
        String version = null;

        assertFalse(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), () -> version));
    }
}