import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();

        // Mocking dependencies
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        oscService.setDatabaseRepository(databaseRepository);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

        // Test method
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(1L);

        assertNotNull(oscLockDatabaseUserInfo);
        assertEquals(1L, oscLockDatabaseUserInfo.getDatabaseId());
        assertTrue(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();

        // Mocking dependencies
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        // Test method
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);

        assertNotNull(oscSwapTableVO);
        assertEquals(1L, oscSwapTableVO.getScheduleTaskId());
        assertFalse(oscSwapTableVO.isManualSwapTableEnabled());
        assertTrue(oscSwapTableVO.isManualSwapTableStarted());
    }
}