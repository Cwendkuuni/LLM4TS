package com.oceanbase.odc.service.onlineschemachange;

import static com.oceanbase.odc.core.shared.constant.ErrorCodes.BadArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionSessionFactory connectionSessionFactory;
    private OscService oscService;

    @Before
    public void setUp() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionSessionFactory = mock(ConnectionSessionFactory.class);
        oscService = new OscService() {
            @Autowired
            public OscService(DatabaseRepository databaseRepository,
                    ScheduleTaskRepository scheduleTaskRepository,
                    ScheduleRepository scheduleRepository, ConnectionService connectionService) {
                super.databaseRepository = databaseRepository;
                super.scheduleTaskRepository = scheduleTaskRepository;
                super.scheduleRepository = scheduleRepository;
                super.connectionService = connectionService;
            }

            @Override
            ConnectionSessionFactory getConnectionSessionFactory() {
                return connectionSessionFactory;
            }
        };
        when(databaseRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabase.isPresent());
        assertSame(id, oscDatabase.get().getDatabaseId());
        assertFalse(oscDatabase.get().isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoNotFound() {
        Long id = 2L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertTrue(oscSwapTable.isPresent());
        assertEquals(scheduleTaskId, oscSwapTable.get().getScheduleTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableInvalidJobType() {
        Long scheduleTaskId = 1L;
        when(scheduleEntity().getJobType())
                .thenReturn(JobType.BACKUP_DATABASE);
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableInvalidSwapTableType() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskEntity().getResultJson())
                .thenReturn("{\"manualSwapTableEnabled\":false}");
        when(scheduleEntity().getJobParametersJson())
                .thenReturn("{\"swapTableType\":\"INVALID\"}");
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(DialectType.ORACLE);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(factory().generateSession())
                .thenReturn(Mockito.mock(ConnectionSession.class));
        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testGetLockUserIsRequiredFalse() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType(DialectType.MYSQL);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(factory().generateSession())
                .thenThrow(new Exception("Exception occurred"));
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    private DatabaseEntity databaseEntity() {
        return new DatabaseEntity();
    }

    private ScheduleEntity scheduleEntity() {
        return new ScheduleEntity();
    }

    private ScheduleTaskEntity scheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }

    private ConnectionSessionFactory factory() {
        return connectionSessionFactory;
    }
}