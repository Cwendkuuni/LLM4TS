import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepositoryMock;
    private ConnectionService connectionServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private ScheduleRepository scheduleRepositoryMock;

    @Before
    public void setUp() {
        oscService = new OscService();
        databaseRepositoryMock = mock(DatabaseRepository.class);
        connectionServiceMock = mock(ConnectionService.class);
        scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        scheduleRepositoryMock = mock(ScheduleRepository.class);

        oscService.setDatabaseRepository(databaseRepositoryMock);
        oscService.setConnectionService(connectionServiceMock);
        oscService.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        oscService.setScheduleRepository(scheduleRepositoryMock);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        when(databaseRepositoryMock.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscLockDatabaseUserInfo);
        assertEquals(databaseEntity.getDatabaseId(), oscLockDatabaseUserInfo.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        onlineSchemaChangeScheduleTaskResult.setManualSwapTableEnabled(true);

        when(scheduleTaskRepositoryMock.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepositoryMock.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(RequestUtils.fromJson(any(), eq(OnlineSchemaChangeParameters.class))).thenReturn(onlineSchemaChangeParameters);
        when(RequestUtils.fromJson(any(), eq(OnlineSchemaChangeScheduleTaskResult.class))).thenReturn(onlineSchemaChangeScheduleTaskResult);

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTableVO);
        assertFalse(onlineSchemaChangeScheduleTaskResult.isManualSwapTableEnabled());
        assertTrue(onlineSchemaChangeScheduleTaskResult.isManualSwapTableStarted());
    }
}