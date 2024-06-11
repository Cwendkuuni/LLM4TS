package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setFieldValue(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.ofNullable(new DatabaseEntity());

        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(1L);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        Long id = -1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");

        Optional<ScheduleEntity> scheduleEntity = Optional.ofNullable(new ScheduleEntity());
        Optional<DatabaseEntity> databaseEntity = Optional.ofNullable(new DatabaseEntity());

        when(databaseRepository.findById(databaseEntity.get().getId())).thenReturn(databaseEntity);
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(scheduleEntity);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(false);
        expectedResult.setManualSwapTableStarted(true);

        ScheduleTaskEntity actualScheduleTask = oscService.swapTable(scheduleTaskId);

        verify(databaseRepository).findById(databaseEntity.get().getId());
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        assertEquals(expectedResult, JsonUtils.fromJson(actualScheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class));

        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);

        OscSwapTableVO actualOscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(expectedOscSwapTable, actualOscSwapTable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        boolean expectedLockUserIsRequired = true;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> {
            DefaultConnectSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
            String version = null;
            ConnectionSession connSession = null;
            try {
                connSession = factory.generateSession();
                version = ConnectionSessionUtil.getVersion(connSession);
            } catch (Exception ex) {
                log.info("Get connection occur error", ex);
            } finally {
                if (connSession != null) {
                    connSession.expire();
                }
            }
            return version;
        })).thenReturn(expectedLockUserIsRequired);

        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

        assertEquals(expectedLockUserIsRequired, actualLockUserIsRequired);
    }
}