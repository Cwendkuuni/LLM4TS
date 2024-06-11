import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
    
    private OscService oscService;

    @Before
    public void setup() {
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscLockDatabaseUserInfo);
        assertEquals(id, oscLockDatabaseUserInfo.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{ \"swapTableType\": \"MANUAL\" }");

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, "{}")).thenReturn(true);

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTableVO);
        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

        boolean lockUserRequired = oscService.getLockUserIsRequired(connectionId);

        assertTrue(lockUserRequired);
    }
}