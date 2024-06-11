package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);
        
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        when(databaseRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(databaseEntity));
        
        oscService.databaseRepository = databaseRepository;
        
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabase.getDatabaseId().longValue());
        assertEquals(false, oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(scheduleTaskEntity));
        
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId().longValue());
    }
}