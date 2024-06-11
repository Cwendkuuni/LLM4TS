
package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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
      ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
      ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
      ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
      ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void shouldGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(9999L);
        when(databaseRepository.findById(any())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(9999L);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
    }

    @Test
    public void shouldSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1000");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(any())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(1000L);
        assertEquals(scheduleTaskEntity.getId(), result.getScheduleTaskId());
    }

    @Test
    public void shouldCheckIfLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        when(connectionService.getForConnectionSkipPermissionCheck(any())).thenReturn(connectionConfig);
        assertFalse(oscService.getLockUserIsRequired(1000L));
    }
}
