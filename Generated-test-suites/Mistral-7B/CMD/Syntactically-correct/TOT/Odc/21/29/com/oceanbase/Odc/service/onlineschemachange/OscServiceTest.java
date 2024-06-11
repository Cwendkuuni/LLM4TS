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
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest
public class OscServiceTest {

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ConnectionService connectionService;

    private OscService oscService;

    @Autowired
    public void setOscService(OscService oscService) {
        this.oscService = oscService;
    }

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        Mockito.when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assertions.assertEquals(id, result.getDatabaseId());
        Assertions.assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assertions.assertNull(result);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("testSchedule");

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        Mockito.when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result)))
                .thenReturn(true);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assertions.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_JobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("testSchedule");

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("");
        scheduleEntity.setJobType(JobType.BACKUP);

        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTable_SwapTableTypeNotManual() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("testSchedule");

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
    }

    @Test
    public void testSwapTable_ManualSwapTableDisabled() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("testSchedule");

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result)))
                .thenReturn(true);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assertions.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        Assertions.assertFalse(result.isManualSwapTableEnabled());
        Assertions.assertTrue(result.isManualSwapTableStarted());
    }
}