import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() {
        // Setup for any necessary initialization before each test
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
        // Add more assertions as needed
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNotNull(result);
        // Add assertions for the returned result
    }
}