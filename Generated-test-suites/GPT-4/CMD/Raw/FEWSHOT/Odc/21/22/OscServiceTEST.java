package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(databaseRepository.getLockUserIsRequired(anyLong())).thenReturn(true);
        
        OscLockDatabaseUserInfo returnedObject = oscService.getOscDatabaseInfo(id);
        assertEquals(databaseEntity.getDatabaseId(), returnedObject.getDatabaseId());
        assertEquals(databaseEntity.getLockDatabaseUserRequired(), returnedObject.getLockDatabaseUserRequired());
    }
    
    @Test
    public void testGetOscDatabaseInfoEmpty() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        
        OscLockDatabaseUserInfo returnedObject = oscService.getOscDatabaseInfo(id);
        assertNull(returnedObject.getDatabaseId());
        assertNull(returnedObject.getLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long taskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        
         // Modify these mocked responses based on your needs.
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO returnedObject = oscService.swapTable(taskId);
        assertEquals(taskId, returnedObject.getScheduleTaskId());
    }
}