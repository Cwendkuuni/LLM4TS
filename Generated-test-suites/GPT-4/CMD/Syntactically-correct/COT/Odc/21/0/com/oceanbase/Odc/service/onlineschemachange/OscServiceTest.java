package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfo_IdNull() {
        Long id = null;
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwapTable_IdNull() {
        Long scheduleTaskId = null;
        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
    }
}