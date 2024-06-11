import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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
import com.oceanbase.odc.service.test.base.AbstractTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class OscServiceTest extends AbstractTest {

    @MockBean
    private DatabaseRepository databaseRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private ConnectionService connectionService;

    @Autowired
    private OscService oscService;

    @Before
    public void setUp() {
        given(databaseRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new DatabaseEntity()));
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleEntity()));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        given(connectionService.getForConnectionSkipPermissionCheck(ArgumentMatchers.anyLong())).willReturn(new ConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        given(databaseRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfo_DatabaseNotPresent() {
        Long id = 1L;
        given(databaseRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());
        oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        given(scheduleEntity.getJobType()).willReturn(JobType.ONLINE_SCHEMA_CHANGE);
        given(scheduleEntity.getJobParametersJson()).willReturn("{}");
        given(databaseRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new DatabaseEntity()));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        given(oscService.getLockUserIsRequired(ArgumentMatchers.anyLong())).willReturn(true);
        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository).updateTaskResult(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(String.class));
        assertEquals(expectedOscSwapTable, oscService.getSwapTableVO(scheduleTaskId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_ScheduleTaskNotPresent() {
        Long scheduleTaskId = 1L;
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_JobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.BACKUP);
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_SwapTableTypeNotManual() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJson(oscParameters));
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_ManualSwapTableNotEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJson(oscParameters));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        given(oscService.getLockUserIsRequired(ArgumentMatchers.anyLong())).willReturn(true);
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        given(scheduleEntity.getJobType()).willReturn(JobType.ONLINE_SCHEMA_CHANGE);
        given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJson(oscParameters));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        OscSwapTableVO expectedOscSwapTable = new OscSwapTableVO();
        expectedOscSwapTable.setScheduleTaskId(scheduleTaskId);
        assertEquals(expectedOscSwapTable, oscService.getSwapTableVO(scheduleTaskId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSwapTableVO_ScheduleTaskNotPresent() {
        Long scheduleTaskId = 1L;
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());
        oscService.getSwapTableVO(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSwapTableVO_SwapTableNotStarted() {
        Long scheduleTaskId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        given(scheduleRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(scheduleEntity));
        given(scheduleEntity.getJobType()).willReturn(JobType.ONLINE_SCHEMA_CHANGE);
        given(scheduleEntity.getJobParametersJson()).willReturn(JsonUtils.toJson(oscParameters));
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        given(scheduleTaskRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(new ScheduleTaskEntity()));
        oscService.getSwapTableVO(scheduleTaskId);
    }
}