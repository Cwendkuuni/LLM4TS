package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
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
    DatabaseRepository databaseRepository;

    @Mock
    ConnectionService connectionService;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    DefaultConnectSessionFactory defaultConnectSessionFactory;

    @Mock
    OscDBUserUtil oscDBUserUtil;

    @InjectMocks
    OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(databaseRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(new ScheduleEntity()));
        when(scheduleTaskRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(Long.valueOf(1L))).thenReturn(new ConnectionConfig());
        when(oscDBUserUtil.isLockUserRequired(Mockito.any(), Mockito.any())).thenReturn(false);
        when(defaultConnectSessionFactory.generateSession()).thenReturn(mock(ConnectionSession.class));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertTrue(oscDatabase.isPresent());
        assertNull(oscDatabase.get());
    }

    @Test
    public void testSwapTable() {
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNull(oscSwapTable);
    }

    @Test
    public void testGetLockUserIsRequired() {
        boolean result = oscService.getLockUserIsRequired(1L);
        assertFalse(result);
    }
}