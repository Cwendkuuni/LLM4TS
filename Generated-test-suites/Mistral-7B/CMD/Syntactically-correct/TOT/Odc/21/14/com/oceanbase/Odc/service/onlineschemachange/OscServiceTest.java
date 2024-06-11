package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@Component
public class OscServiceTest {

    private static final long ID = 1L;
    private static final long SCHEDULE_TASK_ID = 1L;
    private static final long CONNECTION_ID = 1L;
    private static final long DATABASE_ID = 1L;
    private static final long SCHEDULE_ID = 1L;
    private static final String JOB_NAME = "1";

    @Autowired
    private DatabaseRepository databaseRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private OscService oscService;

    @Before
    public void setup() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(DATABASE_ID);
        when(databaseRepository.findById(ID)).thenReturn(Optional.of(databaseEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(SCHEDULE_ID);
        scheduleEntity.setJobName(JOB_NAME);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(SCHEDULE_TASK_ID);
        scheduleTaskEntity.setScheduleId(SCHEDULE_ID);
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(scheduleTaskEntity));

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setConnectionId(CONNECTION_ID);
        when(connectionService.getForConnectionSkipPermissionCheck(CONNECTION_ID))
                .thenReturn(connectionConfig);

        when(databaseRepository.findById(DATABASE_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(ID);
        assertTrue(oscDatabase.isPresent());
        assertEquals(ID, oscDatabase.get().getDatabaseId());
        assertTrue(oscDatabase.get().isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(ID);
        assertFalse(oscDatabase.isPresent());
    }

    @Test
    @Transactional
    public void testSwapTableWithValidParameters() {
        OscSwapTableVO oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertTrue(oscSwapTable.isPresent());
        assertEquals(SCHEDULE_TASK_ID, oscSwapTable.get().getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testSwapTableWithInvalidDatabaseId() {
        when(databaseRepository.findById(DATABASE_ID)).thenReturn(Optional.empty());
        oscService.swapTable(SCHEDULE_TASK_ID);
    }

    @Test
    @Transactional
    public void testSwapTableWithInvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.empty());
        oscService.swapTable(SCHEDULE_TASK_ID);
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testSwapTableWithInvalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(SCHEDULE_ID);
        scheduleEntity.setJobName(JOB_NAME);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_INVALID);
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(SCHEDULE_TASK_ID);
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testSwapTableWithInvalidSwapTableType() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.INVALID);
        when(scheduleRepository.findById(SCHEDULE_ID))
                .thenReturn(Optional.of(new ScheduleEntity().setJobParametersJson(JsonUtils.toJson(oscParameters))));

        oscService.swapTable(SCHEDULE_TASK_ID);
    }

    @Test
    @Transactional
    public void testSwapTableWithManualSwapTableDisabled() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(
                new ScheduleTaskEntity().setResultJson(JsonUtils.toJson(result))));

        OscSwapTableVO oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertNull(oscSwapTable);
    }

    @Test
    @Transactional
    public void testSwapTableWithManualSwapTableEnabled() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(
                new ScheduleTaskEntity().setResultJson(JsonUtils.toJson(result))));

        oscService.swapTable(SCHEDULE_TASK_ID);

        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);
        scheduleTaskRepository.updateTaskResult(SCHEDULE_TASK_ID, JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertTrue(oscSwapTable.isPresent());
        assertEquals(SCHEDULE_TASK_ID, oscSwapTable.get().getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testSwapTableWithInvalidLockUserIsRequired() {
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setConnectionId(CONNECTION_ID);
        when(connectionService.getForConnectionSkipPermissionCheck(CONNECTION_ID))
                .thenReturn(decryptedConnConfig);
        when(databaseRepository.findById(DATABASE_ID)).thenReturn(Optional.of(new DatabaseEntity()));

        oscService.swapTable(SCHEDULE_TASK_ID);
    }
}