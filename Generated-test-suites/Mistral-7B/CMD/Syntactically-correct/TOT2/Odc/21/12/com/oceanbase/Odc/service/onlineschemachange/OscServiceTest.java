package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@Slf4j
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ConnectionService connectionService;

    private OscService oscService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.scheduleTaskRepository = scheduleTaskRepository;
                this.scheduleRepository = scheduleRepository;
                this.connectionService = connectionService;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(id);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotFound() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertNull(actualOscDatabaseInfo);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(true);
        expectedResult.setManualSwapTableStarted(false);

        doReturn(oscParameters).when(oscService).getOscParametersFromJobParameters(scheduleEntity.getJobParametersJson());

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, eq(expectedResult.toJson()));
    }

    @Test
    public void testSwapTable_whenScheduleTaskNotFound() {
        Long scheduleTaskId = 1L;

        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(scheduleRepository, times(1)).findById(1L);
    }

    @Test
    public void testSwapTable_whenDatabaseNotFound() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(scheduleRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        connectionConfig.setDialectType("OBServer");

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connSession = null;
        String version = "3.11.1";

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

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

}