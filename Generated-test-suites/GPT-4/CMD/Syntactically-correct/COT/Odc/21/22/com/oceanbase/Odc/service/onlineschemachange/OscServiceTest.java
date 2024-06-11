package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    DatabaseRepository databaseRepository;
  
    @Mock
    ConnectionService connectionService;
  
    @Mock
    ScheduleTaskRepository scheduleTaskRepository;
  
    @Mock
    ScheduleRepository scheduleRepository;
  
    @InjectMocks
    OscService oscService;
  
    // Testing getOscDatabaseInfo method
    @Test
    public void getOscDatabaseInfoTest() {
        Long id = 1L;
        // Dummy Data
        DatabaseEntity databaseEntity = mock(DatabaseEntity.class);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        // Call the method
        oscService.getOscDatabaseInfo(id);

        verify(databaseRepository).findById(id);
    }

    // Testing swapTable method
    @Test
    public void swapTableTest() {
        Long id = 1L;

        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getJobName()).thenReturn(String.valueOf(id));

        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
        when(onlineSchemaChangeParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        when(result.isManualSwapTableEnabled()).thenReturn(true);

        // Call the method
        oscService.swapTable(id);

        verify(scheduleTaskRepository).findById(id);
        verify(scheduleRepository).findById(id);
    }
  
    // Testing edge case where id doesn't exist in database
    @Test
    public void getOscDatabaseInfoIdDoesNotExistTest() {
        Long id = 1L;

        // This will simulate the ID not existing in the database
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        // Call the method. Since the ID doesn't exist in the database, the resulting OscLockDatabaseUserInfo should have null/empty fields.
        oscService.getOscDatabaseInfo(id);
    }

    // Testing edge case where swapTable method throws Exception
    @Test(expected = Exception.class)
    public void swapTableExceptionTest() {
        Long id = 1L;

        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getJobName()).thenReturn(String.valueOf(id));

        // In this case, it will throw an exception
        when(scheduleTaskRepository.findById(id)).thenThrow(new Exception());

        // Call the method
        oscService.swapTable(id);
    }
}
