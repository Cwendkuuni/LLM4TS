package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.schedule.model.JobType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

  @Mock
  private DatabaseRepository databaseRepository;
  @Mock
  private ConnectionService connectionService;
  @Mock
  private ScheduleTaskRepository scheduleTaskRepository;
  @Mock
  private ScheduleRepository scheduleRepository;

  @InjectMocks
  private OscService oscService;

  @Test
  public void testGetOscDatabaseInfo() {
    DatabaseEntity databaseEntity = new DatabaseEntity();
    databaseEntity.setDatabaseId(1L);
    databaseEntity.setConnectionId(2L);

    when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

    OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

    assertThat(result.getDatabaseId(), is(databaseEntity.getDatabaseId()));
  }

  @Test(expected = PreConditions.NonExistItemException.class)
  public void testGetOscDatabaseInfoNonExist() {
    when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

    oscService.getOscDatabaseInfo(1L);
  }

  @Test
  public void testSwapTable() {
    ScheduleEntity scheduleEntity = new ScheduleEntity();
    scheduleEntity.setJobType("ONLINE_SCHEMA_CHANGE_COMPLETE");
    scheduleEntity.setJobParametersJson("{}");

    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
    scheduleTaskEntity.setResultJson("{}");

    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
    when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

    OscSwapTableVO result = oscService.swapTable(1L);

    assertThat(result.getScheduleTaskId(), is(1L));
  }

  @Test(expected = PreConditions.NonExistItemException.class)
  public void testSwapTableNonExistTask() {
    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

    oscService.swapTable(1L);
  }

  @Test(expected = PreConditions.NonExistItemException.class)
  public void testSwapTableNonExistSchedule() {
    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();

    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
    when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

    oscService.swapTable(1L);
  }
}
