package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

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
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);
        
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        
        assertEquals(1L, oscDatabase.getDatabaseId().longValue());
        assertFalse(oscDatabase.getLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        
        assertEquals(1L, oscSwapTable.getScheduleTaskId().longValue());
    }
}