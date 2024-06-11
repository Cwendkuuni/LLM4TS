import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepositoryMock;
    private ConnectionService connectionServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private ScheduleRepository scheduleRepositoryMock;

    @Before
    public void setup() {
        databaseRepositoryMock = mock(DatabaseRepository.class);
        connectionServiceMock = mock(ConnectionService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        scheduleRepositoryMock = mock(ScheduleRepository.class);

        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepositoryMock);
        oscService.setConnectionService(connectionServiceMock);
        oscService.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        oscService.setScheduleRepository(scheduleRepositoryMock);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidId_ReturnsOscLockDatabaseUserInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        
        when(databaseRepositoryMock.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.getLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId_ReturnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(scheduleTaskRepositoryMock.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepositoryMock.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(Optional.of(new ScheduleEntity()));
        when(connectionServiceMock.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testGetLockUserIsRequired_ExceptionThrown() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();

        when(connectionServiceMock.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        doThrow(new RuntimeException()).when(connectionServiceMock).getForConnectionSkipPermissionCheck(anyLong());

        oscService.getLockUserIsRequired(connectionId);
    }

}