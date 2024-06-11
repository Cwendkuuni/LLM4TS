package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import org.junit.*;

@Service
public class OscServiceTest {

    @Autowired
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 0L; // change this based to a real id in the system
        assertTrue(oscService.getOscDatabaseInfo(id) instanceof OscLockDatabaseUserInfo);
    }

    @Test
    public void testGetOscDatabaseInfo_noDatabaseEntity() {
        Long id = 0L; // change this to an id that does not exist in the system
        assertNotEquals(oscService.getOscDatabaseInfo(id).getDatabaseId(), id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 0L; // change this based to a real scheduleTaskId in the system
        assertTrue(oscService.swapTable(scheduleTaskId) instanceof OscSwapTableVO);
    }

    @Test
    public void testSwapTable_noScheduleTaskEntity() {
        Long scheduleTaskId = 0L; // change this to a scheduleTaskId that does not exist in the system
        Exception exception = assertThrows(PreConditions.NotExistsException.class, () -> {
            oscService.swapTable(scheduleTaskId);
        });
        // check the exception message you want to assert
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 0L; // change this based to a real connectionId in the system
        assertFalse(oscService.getLockUserIsRequired(connectionId)); // change this based on the system requirement
    }

    @Test
    public void testGetLockUserIsRequired_throwException() {
        // This test case can be hard to write using Junit 4 because it depends on the ConnectionSessionFactory.generateSession() to throw an exception.
    }

}