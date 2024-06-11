package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.JobType;
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
 * Test class for OscService
 */
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1L);
        expected.setLockDatabaseUserRequired(true);
        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        assertSame(expected, actual);
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);
        assertNull(actual);
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        oscService.swapTable(scheduleTaskId);

        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_InvalidJobType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobType()).thenReturn(JobType.BACKUP);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        OscDBUserUtil oscDBUserUtil = mock(OscDBUserUtil.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);
        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(true);

        boolean actual = oscService.getLockUserIsRequired(connectionId);
        assertTrue(actual);
        verify(connectionService).getForConnectionSkipPermissionCheck(connectionId);
        verify(oscDBUserUtil).isLockUserRequired(any(), any());

        when(oscDBUserUtil.isLockUserRequired(any(), any())).thenReturn(false);

        actual = oscService.getLockUserIsRequired(connectionId);
        assertFalse(actual);
        verify(oscDBUserUtil).isLockUserRequired(any(), any());
    }

}