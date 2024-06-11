
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@SpringBootTest
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

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);
        Mockito.when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNotNull(oscDatabase);
        assertEquals(Long.valueOf(1L), oscDatabase.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Mockito.when(scheduleRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNotNull(oscSwapTable);
        assertEquals(Long.valueOf(1L), oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connConfig = new ConnectionConfig();
        connConfig.setDialectType(DialectType.MYSQL);
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(connConfig);

        boolean isRequired = oscService.getLockUserIsRequired(1L);

        assertFalse(isRequired);
    }
}
