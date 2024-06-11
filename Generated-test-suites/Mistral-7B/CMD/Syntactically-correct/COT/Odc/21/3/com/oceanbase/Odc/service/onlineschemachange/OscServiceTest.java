package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
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
@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        databaseRepository = Mockito.mock(DatabaseRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    /**
     * Test for getOscDatabaseInfo method with valid input
     */
    @Test
    public void testGetOscDatabaseInfoWithValidInput() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);

        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(1L);
        expectedResult.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(expectedResult, result);

        verify(databaseRepository).findById(id);
    }

    /**
     * Test for getOscDatabaseInfo method with invalid input
     */
    @Test(expected = RuntimeException.class)
    public void testGetOscDatabaseInfoWithInvalidInput() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseOptional = Optional.empty();

        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        oscService.getOscDatabaseInfo(id);
    }

    /**
     * Test for swapTable method with valid input
     */
    @Test
    public void testSwapTableWithValidInput() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(1L);
    }

    /**
     * Test for swapTable method with invalid input
     */
    @Test(expected = RuntimeException.class)
    public void testSwapTableWithInvalidInput() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    /**
     * Test for getLockUserIsRequired method with valid input
     */
    @Test
    public void testGetLockUserIsRequiredWithValidInput() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        String version = "5.6.20";

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(factory.generateSession()).thenReturn(connSession);
        when(ConnectionSessionUtil.getVersion(connSession)).thenReturn(version);

        boolean expectedResult = OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            return factory;
        });

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertEquals(expectedResult, result);
    }

    /**
     * Test for getLockUserIsRequired method with invalid input
     */
    @Test
    public void testGetLockUserIsRequiredWithInvalidInput() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        try {
            oscService.getLockUserIsRequired(connectionId);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}