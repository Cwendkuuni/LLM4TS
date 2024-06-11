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

    @InjectMocks
    private OscService oscService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        verify(databaseRepository, times(1)).findById(id);
        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        verify(databaseRepository, times(1)).findById(id);
        assertNull(result);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();

        Long scheduleId = 1L;
        Optional<ScheduleEntity> scheduleEntity = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntity);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        JsonUtils jsonUtils = mock(JsonUtils.class);
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(jsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        PreConditions preConditions = mock(PreConditions.class);
        when(preConditions.validExists(ResourceType.ODC_SCHEDULE, "schedule ", scheduleId,
                scheduleEntity::isPresent)).thenReturn(true);
        when(preConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", scheduleTaskId,
                scheduleTaskOptional::isPresent)).thenReturn(true);
        when(preConditions.validArgumentState(scheduleEntity.get().getJobType() == JobType.ONLINE_SCHEMA_CHANGE_COMPLETE,
                ErrorCodes.BadArgument, new Object[] {scheduleEntity.get().getJobType()},
                "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name())).thenReturn(true);
        when(preConditions.validArgumentState(oscParameters.getSwapTableType() == SwapTableType.MANUAL,
                ErrorCodes.BadArgument, new Object[] {oscParameters.getSwapTableType()},
                "Swap table type is not " + SwapTableType.MANUAL.name())).thenReturn(true);
        when(preConditions.validArgumentState(result.isManualSwapTableEnabled(),
                ErrorCodes.BadArgument, new Object[] {result.isManualSwapTableEnabled()},
                "Manual Swap table type is not enable "))
                .thenReturn(true);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        OscSwapTableVO resultOscSwapTable = oscService.swapTable(scheduleTaskId);

        verify(databaseRepository, never()).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
        verifyNoMoreInteractions(connectionService);

        assertEquals(scheduleTaskId, resultOscSwapTable.getScheduleTaskId());
        assertNull(resultOscSwapTable.getSchemaId());
        assertNull(resultOscSwapTable.getSourceSchemaName());
        assertNull(resultOscSwapTable.getTargetSchemaName());
        assertNull(resultOscSwapTable.getSwapTableId());
        assertNull(resultOscSwapTable.getSwapTableName());
    }

    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testSwapTable_preConditionFailed() {
        Long scheduleTaskId = 1L;

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleTaskEntity scheduleTask = scheduleTaskOptional.get();

        Long scheduleId = 1L;
        Optional<ScheduleEntity> scheduleEntity = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(scheduleId)).thenReturn(scheduleEntity);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(result));

        JsonUtils jsonUtils = mock(JsonUtils.class);
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(jsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        PreConditions preConditions = mock(PreConditions.class);
        when(preConditions.validExists(ResourceType.ODC_SCHEDULE, "schedule ", scheduleId,
                scheduleEntity::isPresent)).thenReturn(true);
        when(preConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", scheduleTaskId,
                scheduleTaskOptional::isPresent)).thenReturn(true);

        when(preConditions.validArgumentState(scheduleEntity.get().getJobType() == JobType.ONLINE_SCHEMA_CHANGE_COMPLETE,
                ErrorCodes.BadArgument, new Object[] {scheduleEntity.get().getJobType()},
                "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name()))
                .thenReturn(false);

        oscService.swapTable(scheduleTaskId);
    }
}