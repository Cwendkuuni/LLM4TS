
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity de = new DatabaseEntity();
        de.setDatabaseId(1L);
        de.setConnectionId(9999L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(de));
        when(connectionService.getForConnectionSkipPermissionCheck(9999L))
                .thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assert.assertNotNull(result);
        verify(databaseRepository).findById(id);
        verify(connectionService).getForConnectionSkipPermissionCheck(9999L);
    }

    @Test
    public void testSwapTable() {
        Long taskId = 1001L;
        ScheduleTaskEntity ste = new ScheduleTaskEntity();
        ste.setJobName("123456");
        ScheduleEntity se = new ScheduleEntity();
        se.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(ste));
        when(scheduleRepository.findById(123456L)).thenReturn(Optional.of(se));

        OscSwapTableVO result = null;
        try {
            result = oscService.swapTable(taskId);
        } catch (Exception e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getMessage());
        }

        Assert.assertNull(result);
    }
}
