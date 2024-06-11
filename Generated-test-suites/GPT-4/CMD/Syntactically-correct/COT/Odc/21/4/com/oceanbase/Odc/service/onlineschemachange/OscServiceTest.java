package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OscServiceTest {

    @MockBean
    private OscService oscService;

    @Before
    public void setUp() throws Exception {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
        expectedResult.setDatabaseId(1L);
        expectedResult.setLockDatabaseUserRequired(true);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 2L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        try {
            OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(expected = PreConditions.PreConditionException.class)
    public void testInvalidSwapTable() {
        Long scheduleTaskId = 3L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = PreConditions.PreConditionException.class)
    public void testGetOscDatabaseInfoWithInvalidId() {
        Long id = 4L; // This ID does not exist in setup mock repository
        oscService.getOscDatabaseInfo(id);
    }

}
