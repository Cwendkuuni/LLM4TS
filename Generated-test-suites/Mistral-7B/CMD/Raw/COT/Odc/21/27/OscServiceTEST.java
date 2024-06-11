package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yaobin
 */
@Slf4j
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockitoAnnotations.Mock private DatabaseRepository databaseRepository;
    @MockitoAnnotations.Mock private ConnectionService connectionService;
    @MockitoAnnotations.Mock private ScheduleTaskRepository scheduleTaskRepository;
    @MockitoAnnotations.Mock private ScheduleRepository scheduleRepository;

    @Autowired private JsonUtils jsonUtils;

    private Long databaseId;
    private Long scheduleTaskId;
    private Long connectionId;
    private ConnectionConfig decryptedConnConfig;
    private DatabaseEntity databaseEntity;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTask;
    private OnlineSchemaChangeParameters oscParameters;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setup() {
        Mockito.when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));
        Mockito.when(databaseEntity.getDatabaseId()).thenReturn(databaseId);
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));
        Mockito.when(scheduleEntity.getJobId()).thenReturn(scheduleId);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        Mockito.when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotPresent_thenReturnNull() {
        assertNull(oscService.getOscDatabaseInfo(null));
        assertNull(oscService.getOscDatabaseInfo(-1L));
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabasePresent_thenReturnOscDatabaseInfo() {
        databaseId = 1L;
        databaseEntity = new DatabaseEntity();
        oscService.getOscDatabaseInfo(databaseId);
        OscLockDatabaseUserInfo expectedOscDatabase = new OscLockDatabaseUserInfo();
        expectedOscDatabase.setDatabaseId(databaseId);
        expectedOscDatabase.setLockDatabaseUserRequired(true);
        assertNotNull(oscService.getOscDatabaseInfo(databaseId));
        assertNotNull(oscService.getOscDatabaseInfo(databaseId).getDatabaseId());
        assertTrue(oscService.getOscDatabaseInfo(databaseId).isLockDatabaseUserRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenScheduleTaskIsNotFound_thenThrowsException() {
        scheduleTaskId = 1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenDatabaseIsNotFound_thenThrowsException() {
        databaseId = 1L;
        scheduleTaskId = 1L;
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenJobTypeIsNotOnlineSchemaChange_thenThrowsException() {
        databaseId = 1L;
        scheduleTaskId = 1L;
        Mockito.when(scheduleEntity.getJobType()).thenReturn(JobType.BACKUP);
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenSwapTableTypeIsNotManual_thenThrowsException() {
        databaseId = 1L;
        scheduleTaskId = 1L;
        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(jsonUtils.toJson(oscParameters));
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_whenManualSwapTableIsDisabled_thenThrowsException() {
        databaseId = 1L;
        scheduleTaskId = 1L;
        result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        Mockito.when(scheduleTask.getResultJson()).thenReturn(jsonUtils.toJson(result));
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_whenAllConditionsMet_thenReturnOscSwapTableVO() {
        databaseId = 1L;
        scheduleTaskId = 1L;
        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(jsonUtils.toJson(oscParameters));
        result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        Mockito.when(scheduleTask.getResultJson()).thenReturn(jsonUtils.toJson(result));
        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);
        oscService.swapTable(scheduleTaskId);
        assertNotNull(oscService.swapTable(scheduleTaskId));
        assertNotNull(oscService.swapTable(scheduleTaskId).getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionIsPresent_thenReturnTrue() {
        connectionId = 1L;
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionIsNotFound_thenReturnFalse() {
        connectionId = 1L;
        assertFalse(oscService.getLockUserIsRequired(null));
        assertFalse(oscService.getLockUserIsRequired(-1L));
    }
}