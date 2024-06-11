package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.connectionService = connectionService;
                this.scheduleTaskRepository = scheduleTaskRepository;
                this.scheduleRepository = scheduleRepository;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(1L, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());

        DatabaseEntity nullDatabaseEntity = null;
        when(databaseRepository.findById(id)).thenReturn(Optional.ofNullable(nullDatabaseEntity));
        OscLockDatabaseUserInfo nullOscDatabase = oscService.getOscDatabaseInfo(id);
        assertNull(nullOscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("123");
        scheduleTask.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(databaseRepository.findById(Long.parseLong("123"))).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(123L)).thenReturn(Optional.of(new ScheduleEntity()));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test(expected = AssertionError.class)
    public void testSwapTableInvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("123");
        scheduleTask.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        when(scheduleRepository.findById(123L)).thenReturn(Optional.of(new ScheduleEntity()));

        Preconditions.validateArgumentState(
                eq(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE), anyLong(),
                eq(scheduleTask.getJobType()),
                "Task type is not ONLINE_SCHEMA_CHANGE"
        );

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("123");
        scheduleTask.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.BACKUP);

        when(databaseRepository.findById(Long.parseLong("123"))).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(123L)).thenReturn(Optional.of(new ScheduleEntity()));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTableInvalidManualSwapTableEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("123");
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\": false}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        when(databaseRepository.findById(Long.parseLong("123"))).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(123L)).thenReturn(Optional.of(new ScheduleEntity()));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        oscService.swapTable(scheduleTaskId);
    }
}