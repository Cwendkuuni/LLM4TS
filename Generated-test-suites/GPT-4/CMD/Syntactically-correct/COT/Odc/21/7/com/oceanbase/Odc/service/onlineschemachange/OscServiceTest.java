package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.service.onlineschemachange.OscService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.when;

public class OscServiceTest {
    @InjectMocks
    private OscService oscService;

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOscDatabaseInfoNullId() {
        oscService.getOscDatabaseInfo(null);
    }

    @Test
    public void testGetOscDatabaseInfoNonExistentId() {
        when(databaseRepository.findById(-1L)).thenReturn(Optional.empty());
        assertNull(oscService.getOscDatabaseInfo(-1L).getDatabaseId());
    }

    @Test(expected = NullPointerException.class)
    public void testSwapTableNullId(){
        oscService.swapTable(null);
    }

    @Test
    public void testSwapTableNonExistentId() {
        when (scheduleTaskRepository.findById(-1L)).thenReturn(Optional.empty());
        OscSwapTableVO result = oscService.swapTable(-1L);
        assertNull(result.getScheduleTaskId());
    }

    //Further test cases can be written to cover other edge conditions and logic in the swapTable method definition
}
