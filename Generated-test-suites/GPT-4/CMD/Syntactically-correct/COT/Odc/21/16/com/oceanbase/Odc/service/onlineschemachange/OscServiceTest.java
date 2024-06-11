package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class OscServiceTest {

    @InjectMocks
    OscService oscService;

    @Mock
    DatabaseRepository databaseRepository;

    @Mock
    ConnectionService connectionService;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(any(Long.class))).thenReturn(Optional.of(new DatabaseEntity()));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("123");

        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(any(Long.class))).thenReturn(Optional.of(new ScheduleEntity()));

        OscSwapTableVO result = oscService.swapTable(123L);
        Assert.assertNotNull(result);

    }

    // Assuming the getLockUserIsRequired returns boolean and its functionality checks based on connectionId
    @Test
    public void testGetLockUserIsRequired() {
        when(connectionService.getForConnectionSkipPermissionCheck(any(Long.class))).thenReturn(new ConnectionConfig());
        boolean result = oscService.getLockUserIsRequired(123L);
        Assert.assertNotNull(result);
    }

}
