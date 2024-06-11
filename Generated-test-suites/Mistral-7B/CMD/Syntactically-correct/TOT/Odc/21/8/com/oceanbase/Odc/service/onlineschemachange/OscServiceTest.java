package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(databaseRepository.findById(anyLong())).thenAnswer(i -> Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenAnswer(i -> Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(anyLong())).thenAnswer(i -> Optional.of(new ScheduleTaskEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(anyString(), any())).thenReturn(false);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        long id = 1L;
        OscLockDatabaseUserInfo expected = new OscLockDatabaseUserInfo();
        expected.setDatabaseId(1);
        expected.setLockDatabaseUserRequired(false);

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);

        assertEquals(expected, actual);
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotExist() {
        long id = 1L;

        OscLockDatabaseUserInfo actual = oscService.getOscDatabaseInfo(id);

        assertNull(actual);
        verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        long scheduleTaskId = 1L;

        OnlineSchemaChangeScheduleTaskResult expected = new OnlineSchemaChangeScheduleTaskResult();
        expected.setManualSwapTableEnabled(true);
        expected.setManualSwapTableStarted(false);

        OnlineSchemaChangeScheduleTaskResult actual = oscService.swapTable(scheduleTaskId);

        assertNotNull(actual);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(databaseRepository).findById(anyLong());
        verify(connectionService).getForConnectionSkipPermissionCheck(anyLong());
        verify(OscDBUserUtil, never()).isLockUserRequired(anyString(), any());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_InvalidJobType() {
        long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.BACKUP);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidSwapTableType() {
        long scheduleTaskId = 1L;

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_InvalidManualSwapTableEnabled() {
        long scheduleTaskId = 1L;

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(true);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setResultJson(JsonUtils.toJson(result));

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO actual = oscService.swapTable(scheduleTaskId);

        assertNotNull(actual);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(databaseRepository).findById(anyLong());
        verify(connectionService).getForConnectionSkipPermissionCheck(anyLong());
        verify(OscDBUserUtil, never()).isLockUserRequired(anyString(), any());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_InvalidManualSwapTableStarted() {
        long scheduleTaskId = 1L;

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE);
        scheduleEntity.setResultJson(JsonUtils.toJson(result));

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(scheduleTaskId);
    }
}