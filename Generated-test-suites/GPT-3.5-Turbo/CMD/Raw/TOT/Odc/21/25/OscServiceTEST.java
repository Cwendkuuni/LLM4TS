import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Test;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

public class OscServiceTest {

  @Test
  public void testGetOscDatabaseInfo() {
    // Mocking
    DatabaseEntity databaseEntity = new DatabaseEntity();
    databaseEntity.setDatabaseId(1L);
    
    OscService oscService = new OscService();
    oscService.databaseRepository = mock(DatabaseRepository.class);
    
    when(oscService.databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));

    // Test
    OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

    // Verify
    assertEquals((Long)1L, result.getDatabaseId());
  }

  @Test
  public void testSwapTable() {
    // Mocking
    OscService oscService = new OscService();
    oscService.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    oscService.scheduleRepository = mock(ScheduleRepository.class);
    oscService.connectionService = mock(ConnectionService.class);
    
    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
    scheduleTaskEntity.setJobName("1");
    
    when(oscService.scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

    // Test
    OscSwapTableVO result = oscService.swapTable(1L);

    // Verify
    assertEquals((Long)1L, result.getScheduleTaskId());
    
  }

  @Test
  public void testGetLockUserIsRequired() {
    // Mocking
    OscService oscService = new OscService();
    oscService.connectionService = mock(ConnectionService.class);
    ConnectionConfig connectionConfig = new ConnectionConfig();
    when(oscService.connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);

    OscDBUserUtil oscDBUserUtil = mock(OscDBUserUtil.class);
    
    // Test
    boolean result = oscService.getLockUserIsRequired(1L);

    // Verify
    assertEquals(false, result);

  }
}