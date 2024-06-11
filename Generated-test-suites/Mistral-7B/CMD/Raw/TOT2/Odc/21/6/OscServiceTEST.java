import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Slf4j
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ConnectionService connectionService;

    @Autowired
    @InjectMocks
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo_ValidId_ReturnsOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo actualResult = oscService.getOscDatabaseInfo(id);
        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(id);
        assertEquals(expectedResult, actualResult);
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidId_ReturnsEmptyOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actualResult = oscService.getOscDatabaseInfo(id);
        assertTrue(actualResult.isEmpty());
        verify(databaseRepository).findById(id);
    }

    @Test
    @Transactional
    public void testSwapTable_ValidScheduleTaskId_SwapsTableAndReturnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleTaskEntity.setResultJson("");
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO actualResult = oscService.swapTable(scheduleTaskId);

        assertTrue(actualResult.isEmpty());
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult capturedResult = resultCaptor.getValue();
        assertTrue(capturedResult.isManualSwapTableEnabled());
        assertFalse(capturedResult.isManualSwapTableStarted());
    }

    @Test
    @Transactional
    public void testSwapTable_InvalidScheduleTaskId_ThrowsException() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testSwapTable_InvalidJobType_ThrowsException() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleEntity.setJobType(JobType.BACKUP.getValue());
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    @Transactional
    public void testSwapTable_InvalidSwapTableType_ThrowsException() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.DROP);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleTaskEntity.setResultJson("");
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testSwapTable_InvalidManualSwapTableEnabled_ThrowsException() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(scheduleTaskId));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Assertions.assertThrows(RuntimeException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testGetLockUserIsRequired_ValidConnectionId_ReturnsTrue() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        assertTrue(oscService.getLockUserIsRequired(connectionId));
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    @Transactional
    public void testGetLockUserIsRequired_InvalidConnectionId_ReturnsFalse() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(null);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }
}