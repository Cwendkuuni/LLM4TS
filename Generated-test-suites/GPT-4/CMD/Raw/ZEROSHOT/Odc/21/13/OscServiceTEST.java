
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;

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

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity database = new DatabaseEntity();
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(database));
        assertNotNull(oscService.getOscDatabaseInfo(1L));
    }

    @Test(expected = Exception.class)
    public void testSwapTableNotFoundException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        oscService.swapTable(1L);
    }

    @Test(expected = Exception.class)
    public void testSwapTableScheduleNotFoundException() {
        ScheduleTaskEntity scheduleTaskEntity= new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        oscService.swapTable(1L);
    }
    
    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
        scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\": true}");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        assertNotNull(oscService.swapTable(1L));
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Add your mock implementations here
    }
}
