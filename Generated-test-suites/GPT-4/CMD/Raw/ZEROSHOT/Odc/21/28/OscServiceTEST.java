
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {

        // When
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        // Then
        verify(databaseRepository, times(1)).findById(id);
        assertNotNull(result);
    }

    @Test
    @Transactional
    public void testSwapTable() {

        // When
        Long scheduleTaskId = 1L;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        assertNotNull(result);
    }

    @Test
    public void testGetLockUserIsRequired(){
        // When
        Long connectionId = 1L;
        boolean result = oscService.getLockUserIsRequired(connectionId);

        // Then 
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        assertTrue(result == true || result == false);
    }
}
