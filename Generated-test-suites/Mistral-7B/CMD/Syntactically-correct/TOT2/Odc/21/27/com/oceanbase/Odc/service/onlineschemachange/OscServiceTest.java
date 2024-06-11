import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = { OscService.class, DatabaseRepository.class, ScheduleRepository.class,
        ScheduleTaskRepository.class, ConnectionService.class })
@ActiveProfiles("test")
public class OscServiceTest {

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

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertNotNull(oscDatabaseInfo);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());

        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);

        assertNull(oscDatabaseInfo);

        verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.getValue());
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenAnswer(invocation -> {
            ConnectionConfig decryptedConnConfig = new ConnectionConfig();
            decryptedConnConfig.setDialectType("MySQL");
            return decryptedConnConfig;
        });

        DefaultConnectSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenAnswer(invocation -> {
            ConnectionConfig decryptedConnConfig = new ConnectionConfig();
            decryptedConnConfig.setDialectType("MySQL");
            when(factory.generateSession()).thenReturn(connSession);
            return decryptedConnConfig;
        });

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTable);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        verify(databaseRepository).findById(anyLong());
        verify(scheduleRepository).findById(anyLong());
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(connectionService).getForConnectionSkipPermissionCheck(anyLong());
        verify(factory).generateSession();
    }

    @Test
    public void testSwapTable_DatabaseNotFound() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.getValue());
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(databaseRepository).findById(anyLong());
        verify(scheduleRepository).findById(anyLong());
        verifyNoInteractions(scheduleTaskRepository);
        verifyNoInteractions(connectionService);
    }

    @Test
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobType(JobType.BACKUP.getValue());
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE.getValue());
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });

        verify(databaseRepository).findById(anyLong());
        verify(scheduleRepository).findById(anyLong());
        verifyNoInteractions(scheduleTaskRepository);
        verifyNoInteractions(connectionService);
    }

    @AfterEach
    public void afterEach() {
        reset(databaseRepository, scheduleRepository, scheduleTaskRepository, connectionService);
    }
}