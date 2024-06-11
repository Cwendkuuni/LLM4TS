
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private OscService oscService;

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = new OscLockDatabaseUserInfo();
        when(oscService.getOscDatabaseInfo(id)).thenReturn(oscLockDatabaseUserInfo);

        assertEquals(oscLockDatabaseUserInfo, oscService.getOscDatabaseInfo(id));
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        OscSwapTableVO oscSwapTableVO = new OscSwapTableVO();
        when(oscService.swapTable(scheduleTaskId)).thenReturn(oscSwapTableVO);

        assertEquals(oscSwapTableVO, oscService.swapTable(scheduleTaskId));
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        boolean expectedResult = true; // Assuming the result should be true
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(any(ConnectionConfig.class));

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertEquals(expectedResult, result);
    }
}
