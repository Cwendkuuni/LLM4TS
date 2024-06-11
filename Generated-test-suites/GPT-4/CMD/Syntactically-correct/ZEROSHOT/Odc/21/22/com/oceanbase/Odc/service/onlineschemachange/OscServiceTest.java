package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

    private Long id;
    private Long scheduleTaskId;

    @Before
    public void setUp() {
        id = 1L;
        scheduleTaskId = 1L;
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(101L);
        databaseEntity.setConnectionId(101L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId())).thenReturn(null);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
        assertEquals(result.getDatabaseId(), databaseEntity.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName(String.valueOf(scheduleId));
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
        assertEquals(result.getScheduleTaskId(), scheduleTaskId);
    }
}
