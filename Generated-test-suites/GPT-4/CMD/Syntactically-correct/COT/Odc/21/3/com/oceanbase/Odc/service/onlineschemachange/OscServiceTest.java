package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
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

    private Long id = 1L;

    private DatabaseEntity databaseEntity;

    private OscLockDatabaseUserInfo oscDatabase;

    @Before
    public void setUp() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        oscDatabase = new OscLockDatabaseUserInfo();
        oscDatabase.setDatabaseId(id);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo actualResult = oscService.getOscDatabaseInfo(id);

        assertEquals(oscDatabase.getDatabaseId(), actualResult.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_NoDataFound() {
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo actualResult = oscService.getOscDatabaseInfo(id);

        assertEquals(OscLockDatabaseUserInfo.class, actualResult.getClass());
    }

    @Test
    public void testSwapTable() {
        SwapTableType type = SwapTableType.MANUAL;
        boolean isManualSwapTableEnabled = true;
        OscSwapTableVO expected = new OscSwapTableVO();
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName("1");
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(type);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(isManualSwapTableEnabled);
        result.setManualSwapTableStarted(true);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));
        
        when(scheduleTaskRepository.findById(id)).thenReturn(Optional.of(taskEntity));
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO actual = oscService.swapTable(id);

        assertEquals(expected.getManualSwapTableStarted(), actual.getManualSwapTableStarted());
        assertEquals(expected.getScheduleTaskId(), actual.getScheduleTaskId());
    }

    // Add more tests for possible exceptions and edge cases as per your project requirements
}
