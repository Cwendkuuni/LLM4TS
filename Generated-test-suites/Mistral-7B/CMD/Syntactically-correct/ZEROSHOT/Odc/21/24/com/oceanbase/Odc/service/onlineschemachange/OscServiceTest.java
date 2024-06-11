import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeParameters;
import com.oceanbase.odc.metadb.schedule.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * Test class for OscService
 *
 * @author yaobin
 */
@SuppressWarnings("unchecked")
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
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        assertTrue(oscDatabaseInfo.isPresent());
        assertEquals(databaseId, oscDatabaseInfo.get().getDatabaseId());
        assertFalse(oscDatabaseInfo.get().isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long databaseId = 1L;
        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        assertFalse(oscDatabaseInfo.isPresent());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        String jobName = "schedule_" + scheduleId;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(jobName);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobName(jobName);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        String jobName = "schedule_" + scheduleId;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(jobName);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobName(jobName);
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        oscService.swapTable(scheduleTaskId);

        fail("IllegalArgumentException should be thrown");
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(decryptedConnConfig.getDialectType()).thenReturn("OBDC");

        assertTrue(oscService.getLockUserIsRequired(connectionId));

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
        ConnectionSession connSession = null;
        String version = "3.0.0";
        try {
            connSession = factory.generateSession();
            version = ConnectionSessionUtil.getVersion(connSession);
        } finally {
            if (connSession != null) {
                connSession.expire();
            }
        }
        when(factory.generateSession()).thenThrow(new RuntimeException("Test exception"));

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

}