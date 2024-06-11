package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OscServiceTest {

    private DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
    private ConnectionService connectionService = mock(ConnectionService.class);
    
    private OscService oscService = new OscService(databaseRepository, connectionService, 
    scheduleTaskRepository, scheduleRepository);
    
    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong()))
            .thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithNullId() {
        oscService.getOscDatabaseInfo(null);
    }
    
    @Test
    public void testSwapTable() {
        when(scheduleTaskRepository.findById(anyLong()))
            .thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong()))
            .thenReturn(Optional.of(new ScheduleEntity()));
        OscSwapTableVO result = oscService.swapTable(1L);
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidId() {
        when(scheduleTaskRepository.findById(anyLong()))
            .thenReturn(Optional.empty());
        oscService.swapTable(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidConditions() {
        ScheduleEntity se = new ScheduleEntity();
        se.setJobType(JobType.CRON);
        when(scheduleTaskRepository.findById(anyLong()))
            .thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(se));
        oscService.swapTable(1L);
    }
}