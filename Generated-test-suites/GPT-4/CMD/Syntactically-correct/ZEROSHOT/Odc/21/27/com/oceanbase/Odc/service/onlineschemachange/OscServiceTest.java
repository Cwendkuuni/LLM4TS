package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;

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
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> optionalDatabase = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(optionalDatabase);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> optionalScheduleTask = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(optionalScheduleTask);

        ScheduleTaskEntity taskEntity = optionalScheduleTask.get();
        String scheduleIdStr = taskEntity.getJobName();
        Long scheduleId = Long.parseLong(scheduleIdStr);
        Optional<ScheduleEntity> optionalScheduleEntity = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(scheduleId)).thenReturn(optionalScheduleEntity);

        OnlineSchemaChangeParameters oscParameters = 
                     JsonUtils.fromJson(optionalScheduleEntity.get().getJobParametersJson(), OnlineSchemaChangeParameters.class);
                 
        OnlineSchemaChangeScheduleTaskResult result = 
                     JsonUtils.fromJson(taskEntity.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
                 
        when(scheduleTaskRepository.updateTaskResult(any(), any())).thenReturn(1);
        
        OscSwapTableVO swapped = oscService.swapTable(scheduleTaskId);
        
        assertNotNull(swapped);
        assertEquals(scheduleTaskId, swapped.getScheduleTaskId());
    }
}
