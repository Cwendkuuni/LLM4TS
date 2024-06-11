
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;

public class OscServiceTest {
  
  @Test
  public void testGetOscDatabaseInfo() {
    OscService oscService = new OscService();
    OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
    assertNotNull(oscDatabaseInfo);
  }
  
  @Test
  public void testSwapTable() {
    OscService oscService = new OscService();
    OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);
    assertNotNull(oscSwapTableVO);
  }
  
}
