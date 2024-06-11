
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseEntity databaseEntity;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ConnectionConfig connectionConfig;

    @Test
    public void getOscDatabaseInfoTest() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
    }

    @Test
    public void swapTableTest() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn(
            "{\"swapTableType\": \"MANUAL\", \"others\": \"more parameters\"}");
        OscSwapTableVO result = oscService.swapTable(1L);
        assertEquals(1L, result.getScheduleTaskId().longValue());
    }

    @Test
    public void getLockUserIsRequiredTest() {
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        assertTrue(oscService.getLockUserIsRequired(1L));
    }
}
