import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
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
        
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = new OscLockDatabaseUserInfo();
        oscLockDatabaseUserInfo.setDatabaseId(1L);
        oscLockDatabaseUserInfo.setLockDatabaseUserRequired(true);
        
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        
        assertEquals(oscLockDatabaseUserInfo.getDatabaseId(), oscService.getOscDatabaseInfo(id).getDatabaseId());
        assertEquals(oscLockDatabaseUserInfo.isLockDatabaseUserRequired(), oscService.getOscDatabaseInfo(id).isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleTaskId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setScheduleId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        assertEquals(scheduleTaskId, oscService.swapTable(scheduleTaskId).getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("test");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }
}