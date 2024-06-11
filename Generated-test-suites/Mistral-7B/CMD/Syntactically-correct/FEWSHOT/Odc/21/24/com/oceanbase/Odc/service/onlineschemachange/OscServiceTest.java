package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OscServiceTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    private Long databaseId;
    private Long scheduleTaskId;

    @BeforeEach
    public void setup() {
        databaseId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        scheduleTaskId = UUID.randomUUID().getMostSignificantBits();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
        scheduleTaskEntity.setResultJson(new OnlineSchemaChangeScheduleTaskResult().toString());

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
    }

    @Test
    @Transactional
    public void testGetOscDatabaseInfo() {
        Long id = databaseId;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(databaseId);
        expected.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        Assert.assertEquals(expected, actual);

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    @Transactional
    public void testGetOscDatabaseInfoWithLockUserRequired() {
        Long id = databaseId;

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        databaseEntity.setLockDatabaseUserRequired(true);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        Assert.assertEquals(new OscLockDatabaseUserInfo().setLockDatabaseUserRequired(true), actual);

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    @Transactional
    public void testSwapTableWithSuccess() {
        swapTableWithSuccess(false);
    }

    @Test
    @Transactional
    public void testSwapTableWithLockUserRequiredAndSwapTableExists() {
        swapTableWithSuccess(true);
    }

    private void swapTableWithSuccess(boolean lockUserRequired) {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(oscParameters.toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
        scheduleTaskEntity.setResultJson(new OnlineSchemaChangeScheduleTaskResult().toString());

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        Assert.assertEquals(new OscSwapTableVO(), actual);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    @Transactional
    public void testSwapTableWithSwapTableNotExists() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(new OnlineSchemaChangeParameters().toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    @Transactional
    public void testSwapTableWithInvalidSwapTableType() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(oscParameters.toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    @Transactional
    public void testSwapTableWithManualSwapTableNotEnabled() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(oscParameters.toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setScheduleId(scheduleEntity.getId());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));

        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }
}