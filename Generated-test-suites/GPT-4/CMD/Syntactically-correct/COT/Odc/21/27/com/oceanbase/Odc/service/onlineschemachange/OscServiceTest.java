package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.OscService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = anyLong();
        DatabaseEntity databaseEntity = mock(DatabaseEntity.class);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        verify(databaseRepository).findById(anyLong());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = anyLong();
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
        verify(scheduleTaskRepository).findById(anyLong());
        verify(scheduleRepository).findById(anyLong());
    }

    // Due to visibility, the private method cannot be tested directly. However, you can test it through public methods.
}
