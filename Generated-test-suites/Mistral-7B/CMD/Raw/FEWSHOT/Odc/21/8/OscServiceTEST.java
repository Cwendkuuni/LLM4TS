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
import com.oceanbase.odc.metadb.schedule.ScheduleTaskResult;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

/**
 * @author yaobin
 * @date 2023-11-06
 * @since 4.2.3
 */
@SuppressWarnings("unchecked")
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
    private OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createScheduleEntity()));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createScheduleTaskEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(createConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.of(createDatabaseEntity());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_WhenDatabaseNotExist() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = createOnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = createOnlineSchemaChangeScheduleTaskResult();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0) instanceof ConnectionConfig
                        && invocation.getArgument(1).getDialectType().equals("MySQL"));

        oscService.swapTable(scheduleTaskId);

        verify(connectionService).getForConnectionSkipPermissionCheck(eq(scheduleEntity.getConnectionId()));
        verify(databaseRepository).findById(eq(scheduleEntity.getDatabaseId()));
        verify(scheduleTaskRepository).findById(eq(scheduleTaskId));
        verify(scheduleTaskRepository).updateTaskResult(eq(scheduleTaskId), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_WhenDatabaseNotExist() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_WhenNotOnlineSchemaChangeTask() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = createOnlineSchemaChangeParameters();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0) instanceof ConnectionConfig
                        && invocation.getArgument(1).getDialectType().equals("MySQL"));
        when(scheduleEntity.getJobType()).thenReturn(JobType.OFFLINE_SCHEMA_CHANGE);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testSwapTable_WhenManualSwapTableDisabled() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = createOnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = createOnlineSchemaChangeScheduleTaskResult();

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0) instanceof ConnectionConfig
                        && invocation.getArgument(1).getDialectType().equals("MySQL"));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);

        ResultActions actions = doCallRealMethod(oscService, "swapTable", scheduleTaskId);

        actions.thenThrow(IllegalArgumentException.class);
        verifyNoInteractions(scheduleTaskRepository);
    }

    @Test
    public void testSwapTable_WhenSwapTableTypeIsNotManual() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = createOnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = createOnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0) instanceof ConnectionConfig
                        && invocation.getArgument(1).getDialectType().equals("MySQL"));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleTask.getResultJson()).thenReturn("{}");

        ResultActions actions = doCallRealMethod(oscService, "swapTable", scheduleTaskId);

        actions.thenThrow(IllegalArgumentException.class);
        verifyNoInteractions(scheduleTaskRepository);
    }

    @Test
    public void testSwapTable_WhenSwapTableTypeIsManualAndEnabled() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(createScheduleTaskEntity());
        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();
        ScheduleEntity scheduleEntity = createScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = createOnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = createOnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createDatabaseEntity()));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.ofNullable(scheduleTask));
        when(oscDBUserUtil.isLockUserRequired(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0) instanceof ConnectionConfig
                        && invocation.getArgument(1).getDialectType().equals("MySQL"));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE);
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    private DatabaseEntity createDatabaseEntity() {
        return new DatabaseEntity();
    }

    private ScheduleEntity createScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        return scheduleEntity;
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{}");
        return scheduleTaskEntity;
    }

    private ConnectionConfig createConnectionConfig() {
        return new ConnectionConfigBuilder().build();
    }

    private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
        return new OnlineSchemaChangeParameters();
    }

    private OnlineSchemaChangeScheduleTaskResult createOnlineSchemaChangeScheduleTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult();
    }

}