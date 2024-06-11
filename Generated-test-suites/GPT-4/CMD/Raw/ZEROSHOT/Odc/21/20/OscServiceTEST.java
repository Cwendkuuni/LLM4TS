
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.schedule.model.JobType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class OscServiceTest {

    @InjectMocks private OscService oscService;

    @Mock private ConnectionService connectionService;
    @Mock private DatabaseRepository databaseRepository;
    @Mock private ScheduleTaskRepository scheduleTaskRepository;
    @Mock private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long dbId = 123L;
        DatabaseEntity mockDatabaseEntity = new DatabaseEntity();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(mockDatabaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(dbId);

        assertNotNull(result);
    }

    @Test
    public void testSwapTable() throws Exception {
        ScheduleTaskEntity mockScheduleTaskEntity = new ScheduleTaskEntity();
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        mockScheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockScheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(mockScheduleEntity));
        
        OscSwapTableVO result = oscService.swapTable(123L);
        
        assertNotNull(result);
        assertEquals(123L, result.getScheduleTaskId().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_withInvalidJobType() throws Exception {
        ScheduleTaskEntity mockScheduleTaskEntity = new ScheduleTaskEntity();
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobType(JobType.DEFAULT);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(mockScheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(mockScheduleEntity));

        oscService.swapTable(123L);
    }
}
