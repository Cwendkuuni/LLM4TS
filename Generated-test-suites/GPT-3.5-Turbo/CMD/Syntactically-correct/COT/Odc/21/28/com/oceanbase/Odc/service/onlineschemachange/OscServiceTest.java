import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.authority.util.AuthorizeException;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

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

    private OscService oscService;

    @Before
    public void setup() {
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscDatabaseUserInfo = new OscLockDatabaseUserInfo();
        // Mock behavior of DatabaseRepository
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        doReturn(true).when(oscService).getLockUserIsRequired(1L);
        assertEquals(oscDatabaseUserInfo, oscService.getOscDatabaseInfo(1L));
    }

    @Test
    public void testSwapTable() {
        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        assertEquals(oscSwapTable, oscService.swapTable(1L));
    }

    @Test(expected = AuthorizeException.class)
    public void testGetOscDatabaseInfo_InvalidId() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        oscService.getOscDatabaseInfo(1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired("test", () -> "version")).thenReturn(true);
        assertTrue(oscService.getLockUserIsRequired(1L));
    }

}