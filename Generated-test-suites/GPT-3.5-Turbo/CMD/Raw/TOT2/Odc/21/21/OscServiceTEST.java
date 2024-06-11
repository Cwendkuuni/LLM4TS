import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ConnectionService connectionService;

    @Mock
    private OscService oscService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo(){
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(databaseId);

        assertNotNull(oscLockDatabaseUserInfo);
        assertEquals(databaseId, oscLockDatabaseUserInfo.getDatabaseId());
        assertTrue(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable(){
        Long scheduleTaskId = 1L;
        Long scheduleId = 2L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName(String.valueOf(scheduleId));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        // Test swap table logic here
    }

    // Add more test cases for other public methods if needed

}