package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class OscServiceTest {

  @Mock
  private DatabaseRepository databaseRepository;

  @Mock
  private ScheduleTaskRepository scheduleTaskRepository;
  
  @Mock
  private ScheduleRepository scheduleRepository;

  @InjectMocks
  private OscService oscService;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = NullPointerException.class)
  public void testGetOscDatabaseInfoNullId() {
    oscService.getOscDatabaseInfo(null);
  }

  @Test
  public void testGetOscDatabaseInfoNoEntityFound() {
    when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
    OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
    Assert.assertNotNull(result);
  }

  @Test(expected = NullPointerException.class)
  public void testSwapTableNullId() {
    oscService.swapTable(null);
  }
  
  @Test
  public void testSwapTableNoScheduleTaskEntityFound() {
    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
    OscSwapTableVO result = oscService.swapTable(1L);
    Assert.assertNotNull(result);
  }
  
  // Add other test scenarios as necessary, especially for checking valid argument states

}