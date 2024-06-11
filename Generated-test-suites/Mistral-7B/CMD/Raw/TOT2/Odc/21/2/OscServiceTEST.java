package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscService oscService;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.connectionService = connectionService;
                this.scheduleRepository = scheduleRepository;
                this.scheduleTaskRepository = scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(database));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertTrue(result.isPresent());
        assertEquals(database.getDatabaseId(), result.get().getDatabaseId());
        assertFalse(result.get().isLockDatabaseUserRequired());

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        result = oscService.getOscDatabaseInfo(id);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSwapTable_Valid() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setJobParametersJson("{\"param1\":\"value1\"}");
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId,
                eq("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}"));

        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        oscSwapTable.setScheduleTaskId(scheduleTaskId);

        assertEquals(oscSwapTable, oscService.swapTable(scheduleTaskId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setJobParametersJson("{\"param1\":\"value1\"}");
        schedule.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setJobParametersJson("{\"param1\":\"value1\"}");
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidManualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);
        schedule.setJobParametersJson("{\"param1\":\"value1\"}");
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        assertFalse(oscService.swapTable(scheduleTaskId).isPresent());
    }
}