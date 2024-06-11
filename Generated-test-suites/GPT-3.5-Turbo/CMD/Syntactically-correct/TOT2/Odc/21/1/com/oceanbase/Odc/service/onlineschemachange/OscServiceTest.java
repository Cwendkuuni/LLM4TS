import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class OscServiceTest {

    private OscService oscService;
    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        oscService = new OscService();
        databaseRepository = Mockito.mock(DatabaseRepository.class);
        connectionService = Mockito.mock(ConnectionService.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        scheduleRepository = Mockito.mock(ScheduleRepository.class);
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test for valid database Id
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setId(id);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertNotNull(oscDatabaseInfo);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());

        // Test for invalid database Id
        Long invalidId = 100L;
        Mockito.when(databaseRepository.findById(invalidId)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo invalidOscDatabaseInfo = oscService.getOscDatabaseInfo(invalidId);
        assertNotNull(invalidOscDatabaseInfo);
        assertEquals(null, invalidOscDatabaseInfo.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        // To be implemented based on test cases for swapTable method
    }

}