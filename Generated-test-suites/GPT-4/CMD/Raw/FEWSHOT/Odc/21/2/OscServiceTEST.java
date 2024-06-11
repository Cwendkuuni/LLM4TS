
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
        Mockito.when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetOscDatabaseInfoNoDatabase() {
        Mockito.when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNull(result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        // Mock the entities
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        schedule.setJobParametersJson("...");
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        // Define the behavior of the mocked methods
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleTask));
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(schedule));
        Mockito.when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));
        Mockito.when(scheduleEntity.get().getJobParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        
        // Call the method under test and assert the result
        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);
        assertNotNull(oscSwapTableVO);
        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }
}
