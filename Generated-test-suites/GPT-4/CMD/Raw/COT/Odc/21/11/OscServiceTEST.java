import org.junit.Test;
import static org.junit.Assert.*;

public class OscServiceTest {
    OscService oscService = new OscService();

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo info = oscService.getOscDatabaseInfo(123L);
        assertEquals(info.getDatabaseId(), null);
        assertFalse(info.getLockDatabaseUserRequired());
    }

    @Test(expected=PreConditions.ResourceNotFoundException.class)
    public void testSwapTableWithInvalidId() {
        oscService.swapTable(123L);
    }

    @Test(expected=PreConditions.InvalidStateException.class)
    public void testSwapTableWithInvalidJobType() {
        oscService.swapTable(124L);
    }

    @Test(expected=PreConditions.InvalidStateException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        oscService.swapTable(125L);
    }

    @Test(expected=PreConditions.InvalidStateException.class)
    public void testSwapTableWithManualSwapTableDisabled() {
        oscService.swapTable(126L);
    }

    @Test(expected=RuntimeException.class)
    public void testGetLockUserIsRequired() {
        oscService.getLockUserIsRequired(123L);
    }
}