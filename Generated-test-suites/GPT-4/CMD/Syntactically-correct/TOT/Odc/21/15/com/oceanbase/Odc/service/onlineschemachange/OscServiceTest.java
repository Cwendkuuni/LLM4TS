package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {

        Mockito.when(databaseRepository.findById(1L))
        .thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        Assert.assertNotNull(oscDatabaseInfo);
    }
    
    @Test 
    public void testGetOscDatabaseInfo_notPresent(){
   
        Mockito.when(databaseRepository.findById(1L))
        .thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        Assert.assertNotNull(oscDatabaseInfo);  
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfo_nullParameter(){
   
        oscService.getOscDatabaseInfo(null);
    }

    @Test
    public void testSwapTable() {

        // You should mock necessary dependencies for test this method
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        Mockito.when(scheduleTaskRepository.findById(1L))
        .thenReturn(Optional.of(scheduleTaskEntity));
        
        oscService.swapTable(1L);
    } 

    @Test(expected = Exception.class)
    public void testSwapTable_Invalid() {

        // You should mock necessary dependencies for test this method
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        Mockito.when(scheduleTaskRepository.findById(1L))
        .thenReturn(Optional.of(scheduleTaskEntity));
        
        oscService.swapTable(0L);
    } 

}