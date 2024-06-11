package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService();
        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNotNull(result);
        assertEquals(id, result.getDatabaseId());
        // Add more assertions if needed
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 2L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        // Set up mock data for scheduleTaskEntity
        
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertNotNull(result);
        assertEquals(scheduleTaskId, result.getScheduleTaskId());
        // Add more assertions if needed
    }
}