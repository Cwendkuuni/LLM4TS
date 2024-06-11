
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
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

    private Long databaseId = 1L;
    private Long scheduleTaskId = 1L;
    
    @Before
    public void setup() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);
        databaseEntity.setConnectionId(databaseId);
        Optional<DatabaseEntity> opDatabaseEntity = Optional.of(databaseEntity);
        when(databaseRepository.findById(databaseId)).thenReturn(opDatabaseEntity);
    }
    
    @Test
    public void testGetOscDatabaseInfo() {

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(databaseId);
        assertEquals(databaseId, oscDatabase.getDatabaseId());
    }
    
    @Test
    public void testSwapTable() {
    
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(String.valueOf(databaseId));
        Optional<ScheduleTaskEntity> opScheduleTaskEntity = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(opScheduleTaskEntity);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(OnlineSchemaChangeParameters.class));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Optional<ScheduleEntity> opScheduleEntity = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(Long.parseLong(scheduleTaskEntity.getJobName()))).thenReturn(opScheduleEntity);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }
}
