package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Autowired
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
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(true);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(expected.getDatabaseId(), result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
        scheduleEntity.setJobParametersJson("");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(ArgumentMatchers.anyLong())).thenReturn(new ConnectionConfig());
        Mockito.when(OscDBUserUtil.isLockUserRequired(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);

        OscSwapTableVO result = oscService.swapTable(1L);

        assertEquals(expected.getScheduleTaskId(), result.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();

        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        Mockito.when(OscDBUserUtil.isLockUserRequired(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        boolean result = oscService.getLockUserIsRequired(1L);

        assertTrue(result);
    }


}