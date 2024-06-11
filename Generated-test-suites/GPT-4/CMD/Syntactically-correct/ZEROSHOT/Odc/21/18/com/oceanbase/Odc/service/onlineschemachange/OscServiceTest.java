package com.oceanbase.odc.service.onlineschemachange;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.BadArgumentException;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = mock(DatabaseEntity.class);
        when(databaseEntity.getDatabaseId()).thenReturn(100L);
        when(databaseEntity.getConnectionId()).thenReturn(100L);
        when(databaseRepository.findById(100L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(100L);

        assertNotNull(result);
        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
    }

    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getJobName()).thenReturn("100");
        when(scheduleTaskRepository.findById(100L)).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleRepository.findById(100L)).thenReturn(Optional.of(scheduleEntity));

        thrown.expect(BadArgumentException.class);
        thrown.expectMessage("Swap table type is not MANUAL");

        oscService.swapTable(100L);
    }
    
    // Similarly, you can add more test cases to cover other aspects of the code
}
