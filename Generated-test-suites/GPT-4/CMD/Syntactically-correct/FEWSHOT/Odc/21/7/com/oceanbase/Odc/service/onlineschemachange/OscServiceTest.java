package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Optional<DatabaseEntity> databaseEntity = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(anyLong())).thenReturn(databaseEntity);
        assertNotNull(oscService.getOscDatabaseInfo(1L));
        verify(databaseRepository, times(1)).findById(anyLong());
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testGetOscDatabaseInfo_DB_Not_Exists() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNotNull(oscService.getOscDatabaseInfo(1L));
        verify(databaseRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSwapTable() throws Exception
    {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskEntityOptional);
        
        ScheduleEntity scheduleEntity_ = new ScheduleEntity();
        scheduleEntity_.setJobName("1");
        scheduleEntity_.setJobParametersJson("");
        scheduleEntity_.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity_);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);
        
        assertNotNull(oscService.swapTable(1L));
        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }
    
    //Further tests can be continued here
}