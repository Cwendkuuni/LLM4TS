import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ConnectionService connectionService;
    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(createScheduleEntity()));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(createScheduleTaskEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(createConnectionConfig());
        doNothing().when(oscService).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testGetOscDatabaseInfo_ValidDatabaseId_ReturnsExpectedOscDatabaseInfo() {
        Long databaseId = 1L;
        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(databaseId);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidDatabaseId_ReturnsEmptyOscDatabaseInfo() {
        Long databaseId = 1L;

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);
        assertNull(actualOscDatabaseInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidScheduleTaskId_ThrowsIllegalArgumentException() {
        Long scheduleTaskId = -1L;

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId_ReturnsExpectedOscSwapTableVO() {
        Long scheduleTaskId = 1L;

        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);

        OscSwapTableVO actualOscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(expectedOscSwapTable, actualOscSwapTable);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_SwapTableInProgress_ThrowsIllegalStateException() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(createScheduleTaskEntity()));
        when(scheduleTaskRepository.updateTaskResult(anyLong(), anyString())).thenThrow(new IllegalStateException());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired_DialectTypeNotSupportsLockUser_ReturnsFalse() {
        Long connectionId = 1L;
        DialectType dialectType = DialectType.MYSQL;

        boolean actualResult = oscService.getLockUserIsRequired(connectionId);
        assertFalse(actualResult);
    }

    @Test
    public void testGetLockUserIsRequired_DialectTypeSupportsLockUser_ReturnsTrue() {
        Long connectionId = 1L;
        DialectType dialectType = DialectType.ORACLE;

        boolean actualResult = oscService.getLockUserIsRequired(connectionId);
        assertTrue(actualResult);
    }

    private DatabaseEntity createDatabaseEntity() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);
        return databaseEntity;
    }

    private ScheduleEntity createScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("scheduleId");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_SCHEDULE);
        return scheduleEntity;
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setResultJson("{}");
        return scheduleTaskEntity;
    }

    private ConnectionConfig createConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.ORACLE);
        return connectionConfig;
    }
}