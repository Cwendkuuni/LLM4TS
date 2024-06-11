package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@RunWith(MockitoJUnitRunner.class)
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

    private OnlineSchemaChangeParameters oscParameters;
    private ScheduleTaskEntity scheduleTask;
    private ScheduleEntity schedule;

    @Before
    public void setup() {
        oscParameters = new OnlineSchemaChangeParameters();
        schedule = new ScheduleEntity();
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, result.getDatabaseId());
        assertEquals(true, result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_InvalidJobType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.BACKUP);
        schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
    }

    @Test
    public void testSwapTable_InvalidSwapTableType() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters invalidParams = new OnlineSchemaChangeParameters();
        invalidParams.setSwapTableType(SwapTableType.AUTOMATIC);

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
    }

    @Test
    public void testSwapTable_ManualSwapTableDisabled() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
    }

    @Test
    public void testSwapTable_Success() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        oscService.swapTable(1L);

        assertEquals(false, result.isManualSwapTableEnabled());
        assertEquals(true, result.isManualSwapTableStarted());
    }
}