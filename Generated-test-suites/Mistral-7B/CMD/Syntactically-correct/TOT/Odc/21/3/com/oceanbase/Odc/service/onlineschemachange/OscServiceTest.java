package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for OscService
 */
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    private Long databaseId;
    private Long connectionId;
    private Long scheduleTaskId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        databaseId = 1L;
        connectionId = 2L;
        scheduleTaskId = 3L;
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        // Given
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);

        // Then
        assertEquals(null, result.getDatabaseId());
        verify(databaseRepository).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseFound() {
        // Given
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        // When
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);

        // Then
        assertEquals(databaseId, result.getDatabaseId());
        assertEquals(false, result.isLockDatabaseUserRequired());
        verify(databaseRepository).findById(databaseId);
    }

    @Test
    public void testSwapTable_InvalidJobType() {
        // Given
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleEntity.getJobType()).thenReturn(JobType.SCHEDULE_TEST);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // When & Then
       expectedException(IllegalArgumentException.class);
        oscService.swapTable(scheduleTaskId);
        verify(databaseRepository, never()).findAll();
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testSwapTable_ManualSwapTableDisabled() {
        // Given
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleEntity.getJobParametersJson()).thenReturn(getJsonString(new OnlineSchemaChangeParameters()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTask.getResultJson()).thenReturn(getJsonString(new OnlineSchemaChangeScheduleTaskResult()));

        OngoingStubbing<Boolean> manualSwapTableEnabledStub = stub(scheduleTask.getResultJson().isManualSwapTableEnabled());
        manualSwapTableEnabledStub.thenReturn(false);

        // When
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Then
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
        verify(databaseRepository, never()).findAll();
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(connectionId);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, anyString());
        manualSwapTableEnabledStub.thenCallRealMethod();
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, eq("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}"));
    }

    @Test
    public void testSwapTable_ManualSwapTableEnabled() {
        // Given
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleEntity.getJobParametersJson()).thenReturn(getJsonString(new OnlineSchemaChangeParameters()));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTask.getResultJson()).thenReturn(getJsonString(new OnlineSchemaChangeScheduleTaskResult()));

        OngoingStubbing<Boolean> manualSwapTableEnabledStub = stub(scheduleTask.getResultJson().isManualSwapTableEnabled());
        manualSwapTableEnabledStub.thenReturn(true);

        // When
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Then
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
        verify(databaseRepository, never()).findAll();
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(connectionId);
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, anyString());
        manualSwapTableEnabledStub.thenCallRealMethod();
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, eq("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}"));
    }

    @Test
    public void testGetLockUserIsRequired_DatabaseConnectionSuccess() {
        // Given
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());

        // When
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // Then
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(OscDBUserUtil).isLockUserRequired(any(), any());
        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequired_DatabaseConnectionFailure() {
        // Given
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new Exception());

        // When
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // Then
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(OscDBUserUtil, never()).isLockUserRequired(any(), any());
        assertFalse(result);
    }

    private String getJsonString(Object obj) {
        return "{\"jsonData\":" + JsonUtils.toJson(obj) + "}";
    }
}