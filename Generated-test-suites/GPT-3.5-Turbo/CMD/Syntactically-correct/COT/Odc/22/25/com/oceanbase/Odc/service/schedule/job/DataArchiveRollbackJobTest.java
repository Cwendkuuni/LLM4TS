
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    JobExecutionContext jobExecutionContext;

    @Mock
    ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setUp() {
        // Mocking necessary behaviors for tests
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
    }

    @Test
    public void testExecute() {
        // Test execute method functionality
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(scheduleTaskEntity));
        when(scheduleTaskEntity.getResultJson()).thenReturn("{\"taskUnits\": [{\"id\": 1, \"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"PREPARING\"}]}");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.DONE));
    }

    // Add more test cases for edge cases and exceptions
}
