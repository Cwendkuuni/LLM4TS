
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;

public class OscServiceTest {
    
    private OscService oscService;

    @Before
    public void setUp() {
        oscService = new OscService();
    }
    
    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        assertNotNull(oscDatabaseInfo);
    }
    
    @Test
    public void testSwapTable() {
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
    }
}
