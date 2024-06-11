package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
 *
 */
@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockitoAnnotations.Mock
    private DatabaseRepository databaseRepository;

    @MockitoAnnotations.Mock
    private ConnectionService connectionService;

    @MockitoAnnotations.Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockitoAnnotations.Mock
    private ScheduleRepository scheduleRepository;

    private DatabaseEntity databaseEntity;
    private ConnectionConfig connectionConfig;
    private ScheduleEntity scheduleEntity;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskResult scheduleTaskResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setFieldValue(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleRepository", scheduleRepository);

        databaseEntity = new DatabaseEntity();
        connectionConfig = new ConnectionConfig();
        scheduleEntity = new ScheduleEntity();
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskResult = new ScheduleTaskResult();
    }

    @Test
    public void testGetOscDatabaseInfo_success() {
        Long id = 1L;
        PreConditions.validId(id, "database id");
        databaseEntity.setId(id);
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabase.isPresent());
        assertEquals(id, oscDatabase.get().getDatabaseId());
        assertTrue(oscDatabase.get().isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotExist() {
        Long id = 1L;
        oscService.getOscDatabaseInfo(id);
        verify(databaseRepository, times(1)).findById(id);
        assertTrue(Optional.empty().isPresent());
    }

    // Add more test cases for other methods in the class here

    @Test
    public void testSwapTable_success() {
        Long scheduleTaskId = 1L;
        PreConditions.validId(scheduleTaskId, "schedule task id");
        scheduleTaskEntity.setId(scheduleTaskId);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("testJobName");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        Optional<ScheduleEntity> scheduleOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(1L)).thenReturn(scheduleOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        String json = JsonUtils.toJson(oscParameters);
        scheduleEntity.setJobParametersJson(json);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("testJobName");
        scheduleTask.setResultJson(JsonUtils.toJson(scheduleTaskResult));

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        // Add assertions for other fields if necessary
    }

    // Add more test cases for other methods in the class here

    // Test for exceptions and edge cases

    // Test for exceptions:

    // testException1: NullPointerException when getOscDatabaseInfo is called with null id
    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfo_nullId() {
        oscService.getOscDatabaseInfo(null);
    }

    // testException2: Database not found when getOscDatabaseInfo is called with invalid id
    @Test
    public void testGetOscDatabaseInfo_databaseNotExist() {
        Long id = 1L;
        PreConditions.validId(id, "database id");
        databaseEntity.setId(id);
        Optional<DatabaseEntity> databaseOptional = Optional.empty();
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertFalse(oscDatabase.isPresent());
    }

    // Test for edge cases:

    // testEdgeCase1: GetOscDatabaseInfo with maximum allowed id
    @Test
    public void testGetOscDatabaseInfo_maxId() {
        Long maxId = Long.MAX_VALUE;
        databaseEntity.setId(maxId);
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        when(databaseRepository.findById(maxId)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(maxId);
        assertTrue(oscDatabase.isPresent());
        // Add assertions for other fields if necessary
    }

    // Add more test cases for exceptions and edge cases for other methods in the class here

}