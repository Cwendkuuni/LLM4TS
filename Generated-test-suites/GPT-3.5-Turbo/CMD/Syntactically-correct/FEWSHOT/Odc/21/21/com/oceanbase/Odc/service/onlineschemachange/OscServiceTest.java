package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskType;
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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
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
        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo mockOscDatabase = new OscLockDatabaseUserInfo();
        mockOscDatabase.setDatabaseId(1L);
        mockOscDatabase.setLockDatabaseUserRequired(true);
        
        DatabaseEntity mockDatabaseEntity = new DatabaseEntity();
        mockDatabaseEntity.setDatabaseId(1L);
        mockDatabaseEntity.setConnectionId(1L);
        
        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(mockDatabaseEntity));
        
        assertEquals(mockOscDatabase, oscService.getOscDatabaseInfo(1L));
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity mockScheduleTask = new ScheduleTaskEntity();
        mockScheduleTask.setId(1L);
        mockScheduleTask.setJobName("1");
        
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        
        OnlineSchemaChangeParameters mockOscParameters = new OnlineSchemaChangeParameters();
        mockOscParameters.setSwapTableType(SwapTableType.MANUAL);
        
        OnlineSchemaChangeScheduleTaskResult mockResult = new OnlineSchemaChangeScheduleTaskResult();
        mockResult.setManualSwapTableEnabled(true);
        
        ConnectionConfig mockConnectionConfig = new ConnectionConfig();
        mockConnectionConfig.setDialectType("mock");
        
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(mockScheduleTask));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(mockScheduleEntity));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(Mockito.anyLong()))
                .thenReturn(mockConnectionConfig);
        
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        
        assertFalse(mockResult.isManualSwapTableEnabled());
        assertTrue(oscSwapTable.getScheduleTaskId() == 1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig mockConnectionConfig = new ConnectionConfig();
        mockConnectionConfig.setDialectType("mock");
        
        ConnectionSession mockConnSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(factory.generateSession()).thenReturn(mockConnSession);
        Mockito.when(ConnectionSessionUtil.getVersion(mockConnSession)).thenReturn("mockVersion");
        Mockito.when(OscDBUserUtil.isLockUserRequired(Mockito.anyString(), Mockito.any()))
                .thenReturn(true);
        
        assertTrue(oscService.getLockUserIsRequired(1L));
    }

}