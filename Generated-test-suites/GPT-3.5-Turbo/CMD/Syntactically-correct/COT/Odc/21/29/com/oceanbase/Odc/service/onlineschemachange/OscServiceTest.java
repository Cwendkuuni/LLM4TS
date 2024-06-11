
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        oscService.setDatabaseRepository(databaseRepository);

        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        // Add more assertions here as needed
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);

        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        // Add more assertions here as needed
    }

    // Add more test cases for other methods if needed

}
