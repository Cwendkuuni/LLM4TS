import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(id);
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(1L);
        expectedResult.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(expectedResult, result);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotPresent() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable() throws IOException {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.getValue());
        scheduleEntity.setJobParametersJson(IOUtils.toString(getClass().getResourceAsStream("/schedule_task_json.txt")));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);

        OnlineSchemaChangeParameters oscParameters = JsonUtils.fromJson(IOUtils.toString(getClass().getResourceAsStream("/schedule_task_json.txt")),
                OnlineSchemaChangeParameters.class);

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setManualSwapTableEnabled(true);

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(oscDBUserUtil, times(1)).isLockUserRequired(any(), any());

        ScheduleTaskEntity updatedScheduleTaskEntity = new ScheduleTaskEntity();
        updatedScheduleTaskEntity.setId(scheduleTaskId);
        updatedScheduleTaskEntity.setResultJson(IOUtils.toString(getClass().getResourceAsStream("/schedule_task_result_json.txt")));
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, IOUtils.toString(getClass().getResourceAsStream("/schedule_task_result_json.txt")));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(IOUtils.toString(getClass().getResourceAsStream("/schedule_task_result_json.txt")),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() throws IOException {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(3);
        scheduleEntity.setJobParametersJson(IOUtils.toString(getClass().getResourceAsStream("/schedule_task_json.txt")));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        String dialectType = "OBProxy";
        when(oscDBUserUtil.isLockUserRequired(dialectType, any())).thenReturn(true);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequired_NoLockUserRequired() {
        Long connectionId = 1L;

        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        String dialectType = "MySQL";
        when(oscDBUserUtil.isLockUserRequired(dialectType, any())).thenReturn(false);

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertFalse(result);
    }
}