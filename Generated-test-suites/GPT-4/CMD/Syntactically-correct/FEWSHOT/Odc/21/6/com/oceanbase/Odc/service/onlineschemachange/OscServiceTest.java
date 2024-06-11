package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    
    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @InjectMocks
    OscService oscService;
  
    @Test
    public void testGetOscDatabaseInfo() {
      Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
      when(databaseRepository.findById(anyLong())).thenReturn(database);
      OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
      assertNotNull(result);
      //add assert checks for your fields
    }

    @Test(expected=Exceptions.class) //replace Exceptions with the expected type 
    public void testSwapTableWithNonExistentTask() {
      when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());   
      oscService.swapTable(1L);
    }
  
    @Test
    public void testSwapTable() {
      //initialize your mocks and when-then
      //...
      OscSwapTableVO result = oscService.swapTable(1L);
      assertNotNull(result);
      assertEquals(1L, result.getScheduleTaskId().longValue());
      verify(scheduleTaskRepository, times(1)).updateTaskResult(eq(taskId), anyString());
    }

    //repeat the pattern for other cases you want to test
}
