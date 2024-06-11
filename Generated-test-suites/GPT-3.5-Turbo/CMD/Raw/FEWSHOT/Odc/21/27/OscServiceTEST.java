
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        oscService.databaseRepository = databaseRepository;

        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity(1L, 1L)));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
        oscService.connectionService = connectionService;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity(1L, "1", "{}");
        ScheduleEntity scheduleEntity = new ScheduleEntity(1L, 1L, "{}", JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

}
