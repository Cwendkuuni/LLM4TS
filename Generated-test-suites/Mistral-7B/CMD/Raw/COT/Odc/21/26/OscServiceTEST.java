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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
 */
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test getOscDatabaseInfo method

    @Test
    public void testGetOscDatabaseInfo_ValidId_ReturnsOscLockDatabaseUserInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertEquals(1L, oscDatabase.getDatabaseId());
        assertEquals(true, oscDatabase.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidId_ReturnsEmptyOscLockDatabaseUserInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNull(oscDatabase);

        verify(databaseRepository, times(1)).findById(id);
    }

    // Test swapTable method

    @Test
    public void testSwapTable_ValidScheduleTaskId_ReturnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(any(), any()))
                .thenReturn(false);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
        verify(OscDBUserUtil, times(1)).isLockUserRequired(any(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTaskId_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidJobType_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.SCHEDULE_BACKUP);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(connectionService);
        verifyNoMoreInteractions(OscDBUserUtil);

        PreConditions.testBadArgumentState(() -> oscService.swapTable(scheduleTaskId),
                ErrorCodes.BadArgument,
                new Object[] {scheduleEntity.getJobType()},
                "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name());
    }

    @Test
    public void testSwapTable_InvalidSwapTableType_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(connectionService);
        verifyNoMoreInteractions(OscDBUserUtil);

        PreConditions.testBadArgumentState(() -> oscService.swapTable(scheduleTaskId),
                ErrorCodes.BadArgument,
                new Object[] {oscParameters.getSwapTableType()},
                "Swap table type is not " + SwapTableType.MANUAL.name());
    }

    @Test
    public void testSwapTable_InvalidManualSwapTableEnabled_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(any(), any()))
                .thenReturn(false);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
        verify(OscDBUserUtil, times(1)).isLockUserRequired(any(), any());

        PreConditions.testBadArgumentState(() -> oscService.swapTable(scheduleTaskId),
                ErrorCodes.BadArgument,
                new Object[] {result.isManualSwapTableEnabled()},
                "Manual Swap table type is not enable ");
    }
}