
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import java.util.Optional;

public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    ConnectionService connectionService;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetOscDatabaseInfoDatabaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableScheduleTaskNotFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTableScheduleNotFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }
    
    // You could add more tests to check complex edge-cases and potential exceptions.
}
