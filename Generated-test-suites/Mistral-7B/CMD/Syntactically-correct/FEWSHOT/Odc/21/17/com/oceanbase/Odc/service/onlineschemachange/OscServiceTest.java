package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

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
import com.oceanbase.odc.service.session.factory.MockConnectionSessionFactory;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(1L, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_noDatabaseFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNull(result);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_invalidJobType() {
        Long scheduleTaskId = 1L;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("scheduleId");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.SCHEDULE_TASK);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("observer");

        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession session = factory.generateSession();
        String version = "version";

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(factory.generateSession()).thenReturn(session);
        when(ConnectionSessionUtil.getVersion(session)).thenReturn(version);

        assertTrue(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_noSession() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("observer");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        when(factory.generateSession()).thenThrow(new Exception());

        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_noConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("observer");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(ConnectionSessionUtil.getVersion(any())).thenThrow(new Exception());

        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_observerDialect() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("observer");

        ConnectionSessionFactory factory = new MockConnectionSessionFactory();
        String version = "version";

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(factory.generateSession()).thenReturn(mock(ConnectionSession.class));
        when(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).thenReturn(version);
        when(oscDBUserUtil.isLockUserRequired(eq("observer"), any()))
                .thenReturn(Boolean.FALSE);

        assertFalse(oscService.getLockUserIsRequired(1L));
    }

    @Test
    public void testGetLockUserIsRequired_otherDialect() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("other");

        ConnectionSessionFactory factory = new MockConnectionSessionFactory();
        String version = "version";

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(factory.generateSession()).thenReturn(mock(ConnectionSession.class));
        when(ConnectionSessionUtil.getVersion(mock(ConnectionSession.class))).thenReturn(version);
        when(oscDBUserUtil.isLockUserRequired(eq("other"), any()))
                .thenReturn(Boolean.TRUE);

        assertTrue(oscService.getLockUserIsRequired(1L));
    }
}