package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetOscDatabaseInfoDatabaseNotPresent() {
        Long id = 1L;
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableScheduleTaskNotPresent() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    // Repeat similar steps for other edge cases and exceptions
}