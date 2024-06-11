
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private OscService service;
    private DatabaseRepository databaseRepositoryMock;
    private ConnectionService connectionServiceMock;
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    private ScheduleRepository scheduleRepositoryMock;

    @Before
    public void setUp() {
        databaseRepositoryMock = Mockito.mock(DatabaseRepository.class);
        connectionServiceMock = Mockito.mock(ConnectionService.class);
        scheduleTaskRepositoryMock = Mockito.mock(ScheduleTaskRepository.class);
        scheduleRepositoryMock = Mockito.mock(ScheduleRepository.class);

        service = new OscService(databaseRepositoryMock, connectionServiceMock, scheduleTaskRepositoryMock, scheduleRepositoryMock);
    }

    @Test
    public void getOscDatabaseInfoTest() {
        Long mockId = 1L;
        when(databaseRepositoryMock.findById(mockId)).thenReturn(Optional.of(new DatabaseEntity()));

        OscLockDatabaseUserInfo result = service.getOscDatabaseInfo(mockId);

        verify(databaseRepositoryMock).findById(mockId);
        assertNotNull(result);
    }

    @Test
    public void swapTableTest() {
        Long mockId = 1L;
        when(scheduleTaskRepositoryMock.findById(mockId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepositoryMock.findById(mockId)).thenReturn(Optional.of(new ScheduleEntity()));

        OscSwapTableVO result = service.swapTable(mockId);

        verify(scheduleTaskRepositoryMock).findById(mockId);
        verify(scheduleRepositoryMock).findById(mockId);
        assertNotNull(result);
    }

    @Test
    public void getLockUserIsRequiredTest() {
        Long mockId = 1L;
        when(connectionServiceMock.getForConnectionSkipPermissionCheck(mockId)).thenReturn(new ConnectionConfig());

        boolean result = service.getLockUserIsRequired(mockId);

        verify(connectionServiceMock).getForConnectionSkipPermissionCheck(mockId);
        assertNotNull(result);
    }
}
