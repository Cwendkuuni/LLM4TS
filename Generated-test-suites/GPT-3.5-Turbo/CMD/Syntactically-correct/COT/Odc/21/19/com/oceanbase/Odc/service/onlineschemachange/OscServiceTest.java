import com.oceanbase.odc.service.onlineschemachange.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OscServiceTest {

    @Test
    public void testGetOscDatabaseInfo() {
        OscService oscService = new OscService();
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNotNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        OscService oscService = new OscService();
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
    }

    @Test
    public void testGetLockUserIsRequired() {
        OscService oscService = new OscService();
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertNotNull(lockUserRequired);
    }

}