
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.PreConditions;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscServiceTest;

    @Mock
    private ScheduleRepository scheduleRepository;
  
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscDatabaseUserInfo = oscServiceTest.getOscDatabaseInfo(id);
        assertNotNull(oscDatabaseUserInfo);
    }

    @Test(expected = PreConditions.ResourceNotFoundException.class)
    public void testGetOscDatabaseInfo_NotFound() {
        Long id = 99999L; //Non-existent ID
        oscServiceTest.getOscDatabaseInfo(id);
    }

    //... Other tests for methods from OscService
}