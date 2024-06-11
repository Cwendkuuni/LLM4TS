package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

@Slf4j
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private DefaultConnectSessionFactory sessionFactory;
    @Mock
    private ConnectionSessionFactory connectionSessionFactory;
    @Mock
    private ConnectionSession connectionSession;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.expire()).thenReturn(true);
        when(connectionSession.getVersion()).thenReturn("1.0.0");
        when(connectionSessionFactory.newSession(Mockito.any(ConnectionConfig.class))).thenReturn(connectionSession);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(true);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Long id = 1L;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        assertNull(oscService.getOscDatabaseInfo(id));
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setResultJson("{}");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        OscSwapTableVO expected = new OscSwapTableVO();
        expected.setScheduleTaskId(scheduleTaskId);

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);
        assertSame(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTable_databaseNotFound() {
        Long scheduleTaskId = 1L;
        Optional<DatabaseEntity> database = Optional.empty();
        when(databaseRepository.findById(1L)).thenReturn(database);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_jobTypeNotSupported() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("SCHEDULE_JOB_TYPE_NOT_SUPPORTED");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        assertFalse(oscService.swapTable(scheduleTaskId).isPresent());
    }

    @Test
    public void testSwapTable_manualSwapTableDisabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\":false}");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));

        assertFalse(oscService.swapTable(scheduleTaskId).isPresent());
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_dialectTypeNotSupport() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(sessionFactory.generateSession()).thenThrow(new IllegalStateException());

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_noLockUserRequired() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.getVersion()).thenReturn("1.0.0");

        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_lockUserRequired() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(new ConnectionConfig());
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.getVersion()).thenThrow(new IllegalStateException());

        assertTrue(oscService.getLockUserIsRequired(connectionId));
    }
}