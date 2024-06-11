package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;


@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseEntity databaseEntity;
    
    @Mock
    private ScheduleEntity scheduleEntity;
    
    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;    

    @InjectMocks
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseEntity.getConnectionId()).thenReturn(1L);
        when(databaseEntity.getDatabaseId()).thenReturn(1L);
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNotNull(result);
        assertEquals(1L, result.getDatabaseId().longValue());
    }

    @Test
    public void testSwapTable() {       
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"swapTableType\":\"MANUAL\"}");
        when(scheduleTaskEntity.getResultJson()).thenReturn("{\"manualSwapTableEnabled\":true}");
        
        OscSwapTableVO result = oscService.swapTable(1L);

        assertNotNull(result);
        assertEquals(1L, result.getScheduleTaskId().longValue());
    }

    @Test
    public void testGetLockUserIsRequired() {     
        ConnectionConfig config = new ConnectionConfig();        
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(config);
        
        boolean result = oscService.getLockUserIsRequired(1L);

        assertFalse(result);
    }
}