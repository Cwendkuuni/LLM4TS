import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Stubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = OscService.class)
@ActiveProfiles("test")
public class OscServiceTest {

    @MockBean
    private DatabaseRepository databaseRepository;
    @MockBean
    private ConnectionService connectionService;
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockBean
    private ScheduleRepository scheduleRepository;

    @Autowired
    @InjectMocks
    private OscService oscService;

    @BeforeAll
    public static void initMocks() {
        MockitoAnnotations.initMocks(OscServiceTest.class);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidDatabase() {
        Long databaseId = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(databaseId);

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo expectedOscDatabaseInfo = new OscLockDatabaseUserInfo();
        expectedOscDatabaseInfo.setDatabaseId(databaseId);
        expectedOscDatabaseInfo.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        Assertions.assertEquals(expectedOscDatabaseInfo, actualOscDatabaseInfo);

        verify(databaseRepository).findById(databaseId);
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidDatabase() {
        Long databaseId = 1L;

        when(databaseRepository.findById(databaseId)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo actualOscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        Assertions.assertNull(actualOscDatabaseInfo);

        verify(databaseRepository).findById(databaseId);
    }

    @Test
    public void testSwapTable_ValidParameters() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;
        String jsonString = "{}";
        OnlineSchemaChangeParameters oscParameters = JsonUtils.fromJson(jsonString, OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleId(scheduleId);
        scheduleTaskEntity.setJobName(String.valueOf(scheduleId));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(new ScheduleEntity()));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    public void testSwapTable_InvalidScheduleTask() {
        Long scheduleTaskId = 1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        Executable testMethod = () -> oscService.swapTable(scheduleTaskId);

        Assertions.assertThrows(IllegalArgumentException.class, testMethod);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidSchedule() {
        Long scheduleTaskId = 1L;
        Long scheduleId = 1L;

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        Executable testMethod = () -> oscService.swapTable(scheduleTaskId);

        Assertions.assertThrows(IllegalArgumentException.class, testMethod);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    public void testGetLockUserIsRequired_True() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("MySQL");

        Stubbing<ConnectionSession> connectionSessionStubbing = Mockito.stub(connectionService::getForConnectionSkipPermissionCheck)
                .toReturn(decryptedConnConfig);
        String version = "5.6.30";

        when(decryptedConnConfig.getDialectType()).thenReturn(decryptedConnConfig.getDialectType());
        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class))).thenReturn(version);

        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

        Assertions.assertTrue(actualLockUserIsRequired);

        connectionSessionStubbing.verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }

    @Test
    public void testGetLockUserIsRequired_False() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("OBServer");

        Stubbing<ConnectionSession> connectionSessionStubbing = Mockito.stub(connectionService::getForConnectionSkipPermissionCheck)
                .toReturn(decryptedConnConfig);
        String version = "3.0.0";

        when(decryptedConnConfig.getDialectType()).thenReturn(decryptedConnConfig.getDialectType());
        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class))).thenReturn(version);

        boolean actualLockUserIsRequired = oscService.getLockUserIsRequired(connectionId);

        Assertions.assertFalse(actualLockUserIsRequired);

        connectionSessionStubbing.verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
    }
}