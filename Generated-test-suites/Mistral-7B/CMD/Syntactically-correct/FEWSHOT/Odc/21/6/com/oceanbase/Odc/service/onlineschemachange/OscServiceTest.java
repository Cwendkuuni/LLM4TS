package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yaobin
 */
@Service
@Slf4j
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
    @NonNull
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        Optional<DatabaseEntity> database = Optional.of(databaseEntity);

        when(databaseRepository.findById(databaseId)).thenReturn(database);

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(databaseId);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseIsNotFound() {
        Long databaseId = 1L;
        Optional<DatabaseEntity> emptyDatabase = Optional.empty();

        when(databaseRepository.findById(databaseId)).thenReturn(emptyDatabase);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        assertNull(actualOscDatabaseInfo);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableWhenInvalidScheduleTaskId() {
        Long invalidScheduleTaskId = -1L;

        oscService.swapTable(invalidScheduleTaskId);
    }

    @Test
    public void testSwapTableWhenScheduleTaskIsNotFound() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> emptyScheduleTask = Optional.empty();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(emptyScheduleTask);

        oscService.swapTable(scheduleTaskId);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testSwapTableWhenScheduleIsNotFound() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(scheduleId);

        Optional<ScheduleEntity> emptySchedule = Optional.empty();
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(mock(ScheduleTaskEntity.class));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);
        when(scheduleRepository.findById(scheduleId)).thenReturn(emptySchedule);

        oscService.swapTable(scheduleTaskId);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testSwapTableWhenJobTypeIsNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        JobType jobType = JobType.BATCH_JOB;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(scheduleId);
        scheduleEntity.setJobType(jobType);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(mock(ScheduleTaskEntity.class));

        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntityOptional);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
        verify(log, times(1)).info("Task type is not ONLINE_SCHEMA_CHANGE.jobType");
    }

    @Test
    public void testSwapTableWhenSwapTableTypeIsNotManual() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        JobType jobType = JobType.ONLINE_SCHEMA_CHANGE;
        SwapTableType swapTableType = SwapTableType.AUTOMATIC;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(scheduleId);
        scheduleEntity.setJobType(jobType);
        scheduleEntity.setJobParametersJson("{}");

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(mock(ScheduleTaskEntity.class));

        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntityOptional);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
        verify(log, times(1)).info("Swap table type is not MANUAL.swapTableType");
    }

    @Test
    public void testSwapTableWhenManualSwapTableIsDisabled() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        JobType jobType = JobType.ONLINE_SCHEMA_CHANGE;
        SwapTableType swapTableType = SwapTableType.MANUAL;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(scheduleId);
        scheduleEntity.setJobType(jobType);
        scheduleEntity.setJobParametersJson("{\"manualSwapTableEnabled\": false}");

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(false);
        expectedResult.setManualSwapTableStarted(true);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(mock(ScheduleTaskEntity.class));

        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntityOptional);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, eq("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}"));
        verify(log, times(1)).info("Manual Swap table type is not enable ");
    }

    @Test
    public void testSwapTableWhenManualSwapTableIsEnabled() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        JobType jobType = JobType.ONLINE_SCHEMA_CHANGE;
        SwapTableType swapTableType = SwapTableType.MANUAL;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(scheduleId);
        scheduleEntity.setJobType(jobType);
        scheduleEntity.setJobParametersJson("{\"manualSwapTableEnabled\": true}");

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(true);
        expectedResult.setManualSwapTableStarted(false);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(mock(ScheduleTaskEntity.class));

        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntityOptional);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        oscService.swapTable(scheduleTaskId);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository, scheduleRepository);
    }
}