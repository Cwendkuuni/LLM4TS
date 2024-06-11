package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @MockitoAnnotations.Mock
    private DatabaseRepository databaseRepository;
    @MockitoAnnotations.Mock
    private ConnectionService connectionService;
    @MockitoAnnotations.Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @MockitoAnnotations.Mock
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JsonUtils jsonUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for getOscDatabaseInfo method
    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseOptional = Optional.of(new DatabaseEntity());

        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertTrue(oscDatabase == null);

        DatabaseEntity databaseEntity = new DatabaseEntity();
        oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    // Test case for swapTable method
    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(jsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(oscSwapTable, new OscSwapTableVO());
        verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId, "");
    }

    // Test case for getLockUserIsRequired method
    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(decryptedConnConfig);
        when(OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(),
                () -> {
                    DefaultConnectSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
                    ConnectionSession connSession = factory.generateSession();
                    String version = "1.0.0";
                    return version;
                })).thenReturn(false);

        boolean result = oscService.getLockUserIsRequired(connectionId);
        assertFalse(result);
    }
}