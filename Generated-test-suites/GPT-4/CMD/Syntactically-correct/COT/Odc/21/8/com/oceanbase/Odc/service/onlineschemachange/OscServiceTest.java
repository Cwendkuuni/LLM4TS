package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() throws Exception {
        // Setup method
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseEntityOptional = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(databaseEntityOptional);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskEntityOptional);
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
    }

    // Edge cases when getOscDatabaseInfo is passed a null
    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfo_NullInput() {
        oscService.getOscDatabaseInfo(null);
    }

    // Edge case when swapTable is passed a null
    @Test(expected = NullPointerException.class)
    public void testSwapTable_NullInput() {
        oscService.swapTable(null);
    }

    // Exception when database not found in getOscDatabaseInfo
    @Test(expected = NoSuchElementException.class)
    public void testGetOscDatabaseInfo_NotFound() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(1L);
    }

    // Exception when task not found in swapTable
    @Test(expected = NoSuchElementException.class)
    public void testSwapTable_NotFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        oscService.swapTable(1L);
    }
}
