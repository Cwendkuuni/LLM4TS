import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class OscServiceTest {
  
  private DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
  private ConnectionService connectionService = mock(ConnectionService.class);
  private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
  private ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
  
  @Test
  public void testGetOscDatabaseInfo() {
    // Test for valid database ID
    OscService oscService = new OscService();
    Long validId = 1L;
    DatabaseEntity validDatabaseEntity = new DatabaseEntity();
    validDatabaseEntity.setDatabaseId(validId);
    when(databaseRepository.findById(validId)).thenReturn(Optional.of(validDatabaseEntity));
    
    OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(validId);
    assertEquals(validId, result.getDatabaseId());
    
    // Test for non-existent database ID
    Long invalidId = 100L;
    when(databaseRepository.findById(invalidId)).thenReturn(Optional.empty());
    
    OscLockDatabaseUserInfo invalidResult = oscService.getOscDatabaseInfo(invalidId);
    assertEquals(0, invalidResult.getDatabaseId());
  }
  
  @Test
  public void testSwapTable() {
    // Test for valid schedule task ID
    OscService oscService = new OscService();
    Long validScheduleTaskId = 1L;
    ScheduleTaskEntity validScheduleTaskEntity = new ScheduleTaskEntity();
    validScheduleTaskEntity.setJobName("1");
    
    ScheduleEntity validScheduleEntity = new ScheduleEntity();
    validScheduleEntity.setJobParametersJson("{}");
    
    OnlineSchemaChangeScheduleTaskResult validResult = new OnlineSchemaChangeScheduleTaskResult();
    validResult.setManualSwapTableEnabled(true);
    validResult.setManualSwapTableStarted(false);
    
    when(scheduleTaskRepository.findById(validScheduleTaskId)).thenReturn(Optional.of(validScheduleTaskEntity));
    when(scheduleRepository.findById(1L)).thenReturn(Optional.of(validScheduleEntity));
    
    OscSwapTableVO result = oscService.swapTable(validScheduleTaskId);
    assertEquals(validScheduleTaskId, result.getScheduleTaskId());
    
    // Test for invalid schedule task ID
    Long invalidScheduleTaskId = 100L;
    
    when(scheduleTaskRepository.findById(invalidScheduleTaskId)).thenReturn(Optional.empty());
    
    OscSwapTableVO invalidResult = oscService.swapTable(invalidScheduleTaskId);
    assertEquals(0, invalidResult.getScheduleTaskId());
  }
}