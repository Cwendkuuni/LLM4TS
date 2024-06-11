
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        // Setup
        OscService oscService = new OscService();
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        oscService.setDatabaseRepository(databaseRepository);

        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Test
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        // Verify
        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        // Setup
        OscService oscService = new OscService();
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        oscService.setScheduleRepository(scheduleRepository);

        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        Long scheduleId = 2L;
        scheduleTaskEntity.setJobName(String.valueOf(scheduleId));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

        // Test
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        // Verify
        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

}
