package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;

import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

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
	private Long id = 1L;
	private Long scheduleTaskId = 1L;

	@Before
	public void setUp() {
		oscService = new OscService();
	}

	@Test
	public void testGetOscDatabaseInfo() {
		// Given
		Optional<DatabaseEntity> databaseEntityOptional = Mockito.mock(Optional.class);
		when(databaseRepository.findById(id)).thenReturn(databaseEntityOptional);

		long databaseId = 1L;
        OscLockDatabaseUserInfo databaseInfo = new OscLockDatabaseUserInfo();
        databaseInfo.setDatabaseId(databaseId);

		// When
		OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

		// Then
		assertEquals(result, databaseInfo);
	}

	@Test(expected=PreConditions.EntryNotExists.class)
	public void testSwapTableEntryNotExistsException() {
		// Given
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Mockito.mock(Optional.class);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);
        PreConditions.validNotExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task",
                scheduleTaskId, scheduleTaskOptional::isPresent);

		// When
		OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

		// Then exception is thrown
	}

	@Test(expected=PreConditions.BadArgumentException.class)
	public void testSwapTableInvalidParametersException() {
		// Given 
		Optional<ScheduleEntity> scheduleEntityOptional = Mockito.mock(Optional.class);
		when(scheduleRepository.findById(scheduleTaskId)).thenReturn(scheduleEntityOptional);

		// When
		OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

		// Then exception is thrown
	}

	@Test
	public void testGetLockUserIsRequired() {
		// Given 
		Long connectionId = 1L;
		ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
		when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(connectionConfig);

		// When
		boolean result = oscService.getLockUserIsRequired(connectionId);

		// Then
		assertNotNull(result);
	}
}
