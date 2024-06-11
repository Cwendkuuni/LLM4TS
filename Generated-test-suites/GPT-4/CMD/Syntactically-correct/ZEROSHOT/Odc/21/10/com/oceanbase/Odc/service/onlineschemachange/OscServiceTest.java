package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
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

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNotNull(oscDatabase);
        verify(databaseRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSwapTable() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(parameters.toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(result.toString());
        
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);

        assertNotNull(oscSwapTableVO);
        assertTrue(oscSwapTableVO.getScheduleTaskId().equals(scheduleTaskEntity.getScheduleTaskId()));
    }

    @Test(expected = ErrorCodes.BadArgument.class)
    public void testSwapTableSideConditions() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.AUTOMATIC);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(parameters.toString());
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(result.toString());
        
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(1L);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig config = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(config);
        assertTrue(oscService.getLockUserIsRequired(1L));
    }

}
