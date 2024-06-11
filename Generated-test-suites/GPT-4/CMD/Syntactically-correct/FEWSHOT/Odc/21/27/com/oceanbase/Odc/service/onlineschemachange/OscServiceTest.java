package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(id);
        
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(id)).thenReturn(new ConnectionConfig());
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    } 

    @Test
    @Transactional
    public void testSwapTable() {
        Long id = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(id.toString());
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(scheduleEntity));
        
        OscSwapTableVO result = oscService.swapTable(id);
        
        assertNotNull(result);
        assertEquals(id, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long id = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(id)).thenReturn(connectionConfig);
        
        boolean result = oscService.getLockUserIsRequired(id);
        
        assertFalse(result);
    }
}