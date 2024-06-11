package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Autowired
    private DatabaseRepository databaseRepository;
    @Autowired
    private ConnectionService connectionService;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private OscService oscService;
    private DatabaseEntity databaseEntity;
    private ConnectionConfig connectionConfig;
    private ConnectionSessionFactory sessionFactory;
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        oscService = new OscService();
        oscService.databaseRepository = Mockito.spy(databaseRepository);
        oscService.connectionService = Mockito.spy(connectionService);
        oscService.scheduleTaskRepository = Mockito.spy(scheduleTaskRepository);
        oscService.scheduleRepository = Mockito.spy(scheduleRepository);
        databaseEntity = new DatabaseEntity();
        connectionConfig = new ConnectionConfig();
        sessionFactory = new DefaultConnectSessionFactory(connectionConfig);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(true);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);

        assertEquals(expected, actual);

        verify(databaseRepository).findById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOscDatabaseInfoWithInvalidId() {
        Long id = -1L;

        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        oscService.getOscDatabaseInfo(id);
    }

    @Test
    @Transactional
    public void testSwapTable() throws Exception {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobName("testJob");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(databaseRepository.findById(databaseEntity.getDatabaseId())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong())).thenReturn(connectionConfig);
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("version");

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseEntity.getDatabaseId());
        assertEquals(true, oscDatabaseInfo.isLockDatabaseUserRequired());

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);

        assertEquals(OscSwapTableVO.class, actual.getClass());
        assertEquals(scheduleTaskId, actual.getScheduleTaskId());

        verify(databaseRepository).findById(databaseEntity.getDatabaseId());
        verify(scheduleRepository).findById(1L);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(connectionService).getForConnectionSkipPermissionCheck(Mockito.anyLong());
        verify(sessionFactory).generateSession();
        verify(ConnectionSessionUtil).getVersion(connectionSession);

        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        scheduleTaskRepository.updateTaskResult(scheduleTaskId, JsonUtils.toJson(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWithInvalidScheduleTaskId() {
        Long scheduleTaskId = -1L;

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        boolean expected = true;

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(sessionFactory.generateSession()).thenThrow(new Exception("test exception"));

        boolean actual = oscService.getLockUserIsRequired(connectionId);

        assertEquals(expected, actual);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(sessionFactory).generateSession();
    }

    @Test
    public void testGetLockUserIsRequiredWithNoLockUserRequired() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);
        when(sessionFactory.generateSession()).thenReturn(connectionSession);
        when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("version");

        boolean actual = oscService.getLockUserIsRequired(connectionId);

        assertEquals(false, actual);

        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(sessionFactory).generateSession();
        verify(ConnectionSessionUtil).getVersion(connectionSession);
    }
}