package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
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
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(anyLong())).thenReturn(java.util.Optional.of(databaseEntity));
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(expected.getDatabaseId(), result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_invalidId() {
        when(databaseRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(2L);

        assertEquals(0L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(java.util.Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(scheduleTaskRepository.updateTaskResult(anyLong(), anyString())).thenReturn(1);

        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(1L);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertEquals(expected.getScheduleTaskId(), oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_invalidTaskId() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        oscService.swapTable(1L);
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_invalidScheduleId() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(java.util.Optional.of(scheduleTaskEntity));

        when(scheduleRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        oscService.swapTable(1L);
    }

}