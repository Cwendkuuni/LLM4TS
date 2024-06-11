package com.oceanbase.odc.service.onlineschemachange;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for OscService
 */
@Slf4j
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Mock
    private Log log;

    private ConnectionSessionFactory sessionFactory;

    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sessionFactory = mock(ConnectionSessionFactory.class);
        connectionSession = mock(ConnectionSession.class);
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(1L);
        assertNull(actual);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        when(databaseEntity.getConnectionId()).thenReturn(1L);
        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(1L);
        assertSame(databaseEntity.getDatabaseId(), actual.getDatabaseId());
        assertFalse(actual.isLockDatabaseUserRequired());
    }

    @Test(expected = RuntimeException.class)
    @Transactional
    public void testSwapTable_scheduleTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        oscService.swapTable(1L);
    }

    @Test
    @Transactional
    public void testSwapTable_scheduleTaskFound() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleTaskId(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);
        when(result.isManualSwapTableEnabled()).thenReturn(true);

        oscService.swapTable(1L);
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
        assertFalse(result.isManualSwapTableEnabled());
    }
}