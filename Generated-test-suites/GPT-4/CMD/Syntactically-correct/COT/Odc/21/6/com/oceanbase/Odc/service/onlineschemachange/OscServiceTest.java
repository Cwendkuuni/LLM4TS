package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;

public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ConnectionService connectionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Basic Test
    @Test
    public void testGetOscDatabaseInfo() {
       // Mock
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);
        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        Mockito.when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

       // Test
        OscLockDatabaseUserInfo output = oscService.getOscDatabaseInfo(1L);

       // Assertions
        Assert.assertNotNull(output);
        Assert.assertEquals(databaseEntity.getDatabaseId(), output.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
       // Mock
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.MANUAL);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{}", onlineSchemaChangeParameters);
        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskEntity.getManualSwapTableEnabled()).thenReturn(false);
        Mockito.when(scheduleTaskEntity.getResultJson()).thenReturn("{}", onlineSchemaChangeScheduleTaskResult);

       // Test
        OscSwapTableVO output = oscService.swapTable(1L);

       // Assertions
        Assert.assertNotNull(output);
        Assert.assertEquals(scheduleTaskEntity.getJobName(), output.getScheduleTaskId());
    }

    //tests to handle exceptions

    @Test(expected = ErrorCodes.NotFoundException.class)
    public void testGetOscDatabaseInfo_NotFoundException(){
       // Mock
        Mockito.when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        oscService.getOscDatabaseInfo(1L);
    }

    @Test(expected = ErrorCodes.NotFoundException.class)
    public void testSwapTable_NotFoundException(){
        // Mock
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        oscService.swapTable(1L);
    }

    @Test(expected = ErrorCodes.BadArgumentException.class)
    public void testSwapTable_BadArgumentException(){
        // Mock
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.CHECK);
        scheduleEntity.setJobParametersJson("{}", onlineSchemaChangeParameters);
        OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
        Mockito.when(scheduleTaskEntity.getManualSwapTableEnabled()).thenReturn(false);
        Mockito.when(scheduleTaskEntity.getResultJson()).thenReturn("{}", onlineSchemaChangeScheduleTaskResult);

        oscService.swapTable(1L);
    }
}
