import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OscServiceTest {

    @Mock
    DatabaseRepository databaseRepository;

    @Autowired
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        
        assertNotNull(oscDatabase);
        assertEquals(databaseEntity.getDatabaseId(), oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        Mockito.when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        Mockito.when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertTrue(!result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");
        ConnectionSessionFactory connectionSessionFactory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connectionSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        Mockito.when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);

        Mockito.when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), Mockito.any())).thenReturn(true);

        boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);
        
        assertTrue(isLockUserRequired);
    }

}