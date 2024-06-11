package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

    private DatabaseRepository databaseRepository;
    private ScheduleRepository scheduleRepository;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ConnectionService connectionService;
    private OscService oscService;

    @Before
    public void setUp() {
        databaseRepository = mock(DatabaseRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        connectionService = mock(ConnectionService.class);
        oscService = new OscService() {
            {
                this.databaseRepository = databaseRepository;
                this.scheduleRepository = scheduleRepository;
                this.scheduleTaskRepository = scheduleTaskRepository;
                this.connectionService = connectionService;
            }
        };
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo emptyOscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertNull(emptyOscDatabaseInfo);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_IllegalArgumentException() {
        Long scheduleTaskId = 1L;

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_FAILED);

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
        when(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), any())).thenReturn(true);

        assertTrue(oscService.getLockUserIsRequired(1L));

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(OscDBUserUtil.isLockUserRequired(connectionConfig.getDialectType(), any())).thenReturn(false);

        assertFalse(oscService.getLockUserIsRequired(1L));
    }
}