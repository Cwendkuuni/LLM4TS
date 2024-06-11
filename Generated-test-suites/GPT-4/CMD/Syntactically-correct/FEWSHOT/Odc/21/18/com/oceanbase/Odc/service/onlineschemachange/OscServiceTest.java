package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class OscServiceTest {

    @Autowired
    private OscService oscService;
    @Autowired
    private ConnectionService mockConnectionService;
    @Autowired
    private DatabaseRepository mockDatabaseRepository;
    @Autowired
    private ScheduleTaskRepository mockScheduleTaskRepository;
    @Autowired
    private ScheduleRepository mockScheduleRepository;

    // Set up configuration for testing
    @Before
    public void setup() {
        oscService = new OscService();
        oscService.setConnectionService(mockConnectionService);
        oscService.setDatabaseRepository(mockDatabaseRepository);
        oscService.setScheduleTaskRepository(mockScheduleTaskRepository);
        oscService.setScheduleRepository(mockScheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity entity = new DatabaseEntity();
        entity.setDatabaseId(1L);
        when(mockDatabaseRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertEquals("Ensure the database Id matches", result.getDatabaseId(), entity.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName("1");
        when(mockScheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(mockScheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class))).thenReturn(parameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class))).thenReturn(result);

        OscSwapTableVO vo = oscService.swapTable(1L);
        
        assertEquals("Ensure the schedule task Id matches", vo.getScheduleTaskId(), taskEntity.getJobName());
    }
}