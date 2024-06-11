
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OscServiceTest {
  
  @InjectMocks
  private OscService oscService;

  @Mock
  private DatabaseRepository databaseRepository;
  @Mock
  private ConnectionService connectionService;
  
  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testGetOscDatabaseInfo() {
    Long id = 1L;
    Optional<DatabaseEntity> databaseOptional = Optional.empty();
    when(databaseRepository.findById(id)).thenReturn(databaseOptional);
  
    OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
  
    assertTrue(result.getDatabaseId() == null);
    assertFalse(result.isLockDatabaseUserRequired());
  }

  @Test
  public void testSwapTable() {
    Long scheduleTaskId = 1L;

    // assuming valid conditions are met
    when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(new ScheduleTaskEntity()));
    when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
    when(JsonUtils.fromJson(any(), eq(OnlineSchemaChangeParameters.class)))
            .thenReturn(new OnlineSchemaChangeParameters());
    when(JsonUtils.fromJson(any(), eq(OnlineSchemaChangeScheduleTaskResult.class)))
            .thenReturn(new OnlineSchemaChangeScheduleTaskResult());
  
    OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
  
    assertNotNull(result);
    assertEquals(scheduleTaskId, result.getScheduleTaskId());
  }
  
  // add more test methods here...
}
