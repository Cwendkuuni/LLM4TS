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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yaobin
 */
@Slf4j
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private DefaultConnectSessionFactory connectionSessionFactory;

    @InjectMocks
    @NonNull
    private OscService oscService;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Transactional
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(false);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoWhenDatabaseNotExist() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertNull(oscDatabase);
        verify(databaseRepository, times(1)).findById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWhenScheduleTaskNotFound() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        oscService.swapTable(scheduleTaskId);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result).getBytes());
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters).getBytes());
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenAnswer(invocation -> {
            ConnectionConfig decryptedConnConfig = new ConnectionConfig();
            decryptedConnConfig.setDialectType("MySQL");
            return decryptedConnConfig;
        });
        when(OscDBUserUtil.isLockUserRequired(eq("MySQL"), any())).thenReturn(false);
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        ConnectionSessionUtil.Stub connectionSessionUtil = new ConnectionSessionUtil.Stub();
        when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
        connectionSessionUtil.setConnectionSession(connectionSession);
        when(OscDBUserUtil.getVersion(connectionSession)).thenReturn("5.7.23-log");
        when(factory.generateSession()).thenReturn(connectionSession);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(Long.parseLong(scheduleTask.getJobName()));
        verify(scheduleTaskRepository, times(1)).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTableWhenJobTypeNotOnlineSchemaChange() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobType()).thenReturn(JobType.BACKUP);
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        oscService.swapTable(scheduleTaskId);
    }

}