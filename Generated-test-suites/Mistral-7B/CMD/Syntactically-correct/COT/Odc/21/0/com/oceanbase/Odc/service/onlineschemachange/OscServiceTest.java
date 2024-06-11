package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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

/**
 * OscService Test
 */
@SuppressWarnings("unchecked")
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SkipAuthorize skipAuthorize;

    @Mock
    private DefaultConnectSessionFactory connectionSessionFactory;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    private Long testDatabaseId;
    private Long testConnectionId;
    private Long testScheduleTaskId;
    private Long testScheduleId;
    private ConnectionConfig testConnectionConfig;
    private DatabaseEntity testDatabaseEntity;
    private ScheduleEntity testScheduleEntity;
    private ScheduleTaskEntity testScheduleTaskEntity;
    private OnlineSchemaChangeParameters testOscParameters;
    private OnlineSchemaChangeScheduleTaskResult testResult;
    private OscSwapTableVO testOscSwapTable;
    private OscLockDatabaseUserInfo testOscDatabaseInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testDatabaseId = 1L;
        testConnectionId = 2L;
        testScheduleTaskId = 3L;
        testScheduleId = 4L;
        testConnectionConfig = new ConnectionConfig();
        testDatabaseEntity = new DatabaseEntity();
        testScheduleEntity = new ScheduleEntity();
        testScheduleTaskEntity = new ScheduleTaskEntity();
        testOscParameters = new OnlineSchemaChangeParameters();
        testResult = new OnlineSchemaChangeScheduleTaskResult();
        testOscSwapTable = new OscSwapTableVO();
        testOscDatabaseInfo = new OscLockDatabaseUserInfo();
    }

    /**
     * Test getOscDatabaseInfo method with valid database id.
     */
    @Test
    public void testGetOscDatabaseInfoWithValidDatabaseId() {
        when(databaseRepository.findById(testDatabaseId)).thenReturn(Optional.of(testDatabaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(testDatabaseId);

        verify(databaseRepository).findById(testDatabaseId);
        assertEquals(testDatabaseId, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());

        verifyNoMoreInteractions(databaseRepository);
    }

    /**
     * Test getOscDatabaseInfo method with invalid database id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithInvalidDatabaseId() {
        when(databaseRepository.findById(testDatabaseId)).thenReturn(Optional.empty());

        oscService.getOscDatabaseInfo(testDatabaseId);

        verify(databaseRepository).findById(testDatabaseId);
        verifyNoInteractions(databaseRepository);
    }

    /**
     * Test swapTable method with valid schedule task id.
     */
    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        when(scheduleTaskRepository.findById(testScheduleTaskId)).thenReturn(Optional.of(testScheduleTaskEntity));
        when(scheduleRepository.findById(testScheduleId)).thenReturn(Optional.of(testScheduleEntity));
        when(databaseRepository.findById(testScheduleEntity.getDatabaseId())).thenReturn(Optional.of(testDatabaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(testConnectionId))
                .thenReturn(testConnectionConfig);
        when(oscDBUserUtil.isLockUserRequired(testConnectionConfig.getDialectType(), () -> {
            ConnectionSessionFactory factory = connectionSessionFactory;
            String version = null;
            ConnectionSession connSession = null;
            try {
                connSession = factory.generateSession();
                version = ConnectionSessionUtil.getVersion(connSession);
            } catch (Exception e) {
                log.info("Get connection occur error", e);
            } finally {
                if (connSession != null) {
                    connSession.expire();
                }
            }
            return version;
        })).thenReturn(true);

        OscSwapTableVO result = oscService.swapTable(testScheduleTaskId);

        verify(scheduleTaskRepository).findById(testScheduleTaskId);
        verify(scheduleRepository).findById(testScheduleId);
        verify(databaseRepository).findById(testScheduleEntity.getDatabaseId());
        verify(connectionService).getForConnectionSkipPermissionCheck(testConnectionId);
        verify(oscDBUserUtil).isLockUserRequired(testConnectionConfig.getDialectType(), any());
        assertEquals(testScheduleTaskId, result.getScheduleTaskId());

        verifyNoMoreInteractions(databaseRepository);
        verifyNoMoreInteractions(connectionService);
    }

    /**
     * Test swapTable method with invalid schedule task id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(testScheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(testScheduleTaskId);

        verify(scheduleTaskRepository).findById(testScheduleTaskId);
        verifyNoInteractions(scheduleTaskRepository);
    }
}