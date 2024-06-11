package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleRepository(scheduleRepository);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        Optional<DatabaseEntity> database = Optional.of(databaseEntity);

        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertTrue(result.equals(new OscLockDatabaseUserInfo()));

        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertTrue(result.equals(new OscLockDatabaseUserInfo()));

        verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO result1 = oscService.swapTable(scheduleTaskId);
        assertTrue(result1.equals(new OscSwapTableVO()));

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleEntity.getJobName()));
        verify(JsonUtils::fromJson).withArguments(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        verify(JsonUtils::fromJson).withArguments(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_JobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.BACKUP_SCHEDULE);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_SwapTableTypeNotManual() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_ManualSwapTableNotEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("scheduleId");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        oscService.swapTable(scheduleTaskId);
    }
}