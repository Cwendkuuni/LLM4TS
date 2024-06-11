package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oscService = new OscService();
        oscService.setDatabaseRepository(databaseRepository);
        oscService.setConnectionService(connectionService);
        oscService.setScheduleTaskRepository(scheduleTaskRepository);
        oscService.setScheduleRepository(scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long databaseId = 1L;

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(databaseId);

        assertNotNull(oscDatabaseInfo);
        assertEquals(databaseId, oscDatabaseInfo.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(scheduleTaskId);

        assertNotNull(oscSwapTableVO);
        assertEquals(scheduleTaskId, oscSwapTableVO.getScheduleTaskId());
        assertTrue(oscSwapTableVO.isManualSwapTableStarted());
    }

}