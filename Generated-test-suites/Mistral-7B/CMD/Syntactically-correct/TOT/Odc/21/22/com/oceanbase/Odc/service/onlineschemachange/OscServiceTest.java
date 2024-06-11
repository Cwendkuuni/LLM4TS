import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.StubbingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.JobType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ConnectionService connectionService;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.testee = testee;
    }

    @After
    public void tearDown() {
        reset(databaseRepository, connectionService, scheduleRepository, scheduleTaskRepository, oscDBUserUtil);
    }

    @Test
    public void testGetOscDatabaseInfo_validDatabaseId_returnDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = testee.getOscDatabaseInfo(id);
        assertEquals(id, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_invalidDatabaseId_returnNull() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = testee.getOscDatabaseInfo(id);
        assertNull(result);

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable_validScheduleTaskId_returnSwapTableVO() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenReturn(false);

        OscSwapTableVO result = testee.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_invalidScheduleTaskId_throwException() {
        Long scheduleTaskId = 1L;

        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> testee.swapTable(scheduleTaskId));

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_invalidJobType_throwException() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.BACKUP);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\"}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> testee.swapTable(scheduleTaskId));

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_invalidSwapTableType_throwException() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"INVALID\"}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> testee.swapTable(scheduleTaskId));

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_manualSwapTableDisabled_returnSwapTableVO() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\",\"ManualSwapTableEnabled\":false}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenReturn(false);

        OscSwapTableVO result = testee.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testSwapTable_manualSwapTableEnabled_throwException() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setJobParametersJson("{\"SwapTableType\":\"MANUAL\",\"ManualSwapTableEnabled\":true}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> testee.swapTable(scheduleTaskId));

        verify(databaseRepository, never()).findAllById(any());
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
        verify(scheduleTaskRepository, never()).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequired_supportableDialect_returnTrue() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(oscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), any()))
                .thenReturn(true);

        assertTrue(testee.getLockUserIsRequired(connectionId));

        verify(oscDBUserUtil, times(1)).isLockUserRequired(decryptedConnConfig.getDialectType(), any());
    }

    @Test
    public void testGetLockUserIsRequired_unsupportableDialect_returnFalse() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL5.6");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(oscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), any()))
                .thenReturn(false);

        assertFalse(testee.getLockUserIsRequired(connectionId));

        verify(oscDBUserUtil, times(1)).isLockUserRequired(decryptedConnConfig.getDialectType(), any());
    }

}