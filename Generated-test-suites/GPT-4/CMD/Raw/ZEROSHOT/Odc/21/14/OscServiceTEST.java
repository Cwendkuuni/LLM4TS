
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.schedule.model.JobType;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;
import org.junit.Assert;

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
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity dummyDatabaseEntity = new DatabaseEntity();
        dummyDatabaseEntity.setDatabaseId(id);
        dummyDatabaseEntity.setConnectionId(id);
        given(databaseRepository.findById(id)).willReturn(Optional.of(dummyDatabaseEntity));
        given(connectionService.getForConnectionSkipPermissionCheck(id)).willReturn(new ConnectionConfig());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
    }

    @Test(expected = ErrorCodes.class)
    public void testSwapTableWithInvalidScheduleTask() {
        ScheduleTaskEntity dummyScheduleEntity = new ScheduleTaskEntity();
        dummyScheduleEntity.setJobName("dummy");
        given(scheduleTaskRepository.findById(1L)).willReturn(Optional.of(dummyScheduleEntity));
        oscService.swapTable(1L);
    }

    @Test(expected = ErrorCodes.class)
    public void testSwapTableWithInvalidOnlineSchemaChange() {
        ScheduleTaskEntity dummyScheduleEntity = new ScheduleTaskEntity();
        dummyScheduleEntity.setJobName("dummy");
        given(scheduleTaskRepository.findById(1L)).willReturn(Optional.of(dummyScheduleEntity));
        given(scheduleRepository.findById(dummyScheduleEntity.getJobName()).get().getJobType())
            .willReturn(JobType.OTHER);
        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity dummyScheduleEntity = new ScheduleTaskEntity();
        dummyScheduleEntity.setJobName("dummy");
        given(scheduleTaskRepository.findById(1L)).willReturn(Optional.of(dummyScheduleEntity));
        given(scheduleRepository.findById(dummyScheduleEntity.getJobName()).get().getJobType())
            .willReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        oscService.swapTable(1L);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        Assert.assertFalse(result.isManualSwapTableEnabled());
        Assert.assertTrue(result.isManualSwapTableStarted());
    }
}
