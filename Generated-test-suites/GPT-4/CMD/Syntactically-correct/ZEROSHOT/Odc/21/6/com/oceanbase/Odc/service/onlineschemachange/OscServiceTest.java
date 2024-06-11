package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.*;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getOscDatabaseInfoTest() {
        DatabaseEntity entity = new DatabaseEntity();
        entity.setConnectionId(1L);
        entity.setDatabaseId(2L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(connectionService.getForConnectionSkipPermissionCheck(any())).thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertEquals(entity.getDatabaseId(), result.getDatabaseId());
    }
    
    @Test
    public void swapTableTest() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("123");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(1L);
        assertEquals(1L, result.getScheduleTaskId().longValue());
    }
    
    @Test(expected = RuntimeException.class)
    public void swapTable_invalidJobType() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("123");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.LOG_COLLECTION);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(1L);
    }
}
