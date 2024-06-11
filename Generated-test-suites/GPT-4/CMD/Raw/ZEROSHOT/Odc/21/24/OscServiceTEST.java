import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ConnectionService connectionService;
    @InjectMocks
    OscService oscService;


    @Before
    public void setUp() {

    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(100L);
        when(databaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(databaseEntity));
        where(OscDBUserUtil.isLockUserRequired(Mockito.any(ConnectionDialectType.class), Mockito.any(Checkable.class)))
            .thenReturn(true);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(100L);

        Assert.assertNotNull(oscDatabase);
        Assert.assertTrue(oscDatabase.isLockDatabaseUserRequired());
        Assert.assertEquals(100L, oscDatabase.getDatabaseId());
    }

    @Test
    public void testSwapTable() {

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("100");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        where(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        where(scheduleTaskEntity.getResultJson()).thenReturn(JsonUtils.toJson(result));

        where(scheduleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleEntity));

        where(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(100L);

        Assert.assertNotNull(oscSwapTableVO);
        Assert.assertEquals(100L, oscSwapTableVO.getScheduleTaskId());
    }
}