package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

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
        //Add setup code if required.
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(id))
                .thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabase);
        assertEquals(id, oscDatabase.getDatabaseId());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable(){
        Long id = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(id+"");
        
        when(scheduleTaskRepository.findById(id))
                .thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(id);

        assertNotNull(oscSwapTableVO);
        assertEquals(id, oscSwapTableVO.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(id);
    }

}