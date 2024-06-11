package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.schedule.model.JobType;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void getOscDatabaseInfoTest() {
        
        DatabaseEntity dbEntity = new DatabaseEntity();
        dbEntity.setDatabaseId(1L);
        
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(dbEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals((Long)1L, result.getDatabaseId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void swapTableTest() {

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity resultEntity = new ScheduleEntity();

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(1L);

        assertEquals((Long)1L, result.getScheduleTaskId());
    }
}