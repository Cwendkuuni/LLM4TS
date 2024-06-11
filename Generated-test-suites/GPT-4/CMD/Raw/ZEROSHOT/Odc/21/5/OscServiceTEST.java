
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(new DatabaseEntity()));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNotNull(result);
    }

    @Test
    public void testSwapTable() {
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new ScheduleEntity()));

        OscSwapTableVO result = oscService.swapTable(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

        boolean result = oscService.getLockUserIsRequired(1L);
        assertNotNull(result);
    }
}
