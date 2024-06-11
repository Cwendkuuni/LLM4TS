import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.dlm.model.DlmTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {
    private static final Long TEST_TASK_ID = 1L;
    private static final String TEST_JSON = "{}";
    private static final TaskStatus TEST_STATUS = TaskStatus.PREPARING;

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private DlmTask dlmTask;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        rollbackParameters = new DataArchiveRollbackParameters();

        // Mock the entity methods to return the test constants
        when(taskEntity.getId()).thenReturn(TEST_TASK_ID);
        when(taskEntity.getParametersJson()).thenReturn(TEST_JSON);

        dlmTask = Mockito.mock(DlmTask.class);
    }

    @Test
    public void testExecute() {
        rollbackParameters.setDataArchiveTaskId(TEST_TASK_ID);
        when(taskEntity.getParametersJson()).thenReturn(TEST_JSON);
        when(dlmTask.getStatus()).thenReturn(TEST_STATUS);
        when(dlmTask.getSourceDatabaseId()).thenReturn(TEST_TASK_ID);

        // Execute the method to test
        dataArchiveRollbackJob.execute(null);

        // Assertions to validate method functionality
        Mockito.verify(taskEntity, Mockito.times(2)).getId();
        Mockito.verify(dlmTask, Mockito.times(1)).getStatus();
        Mockito.verify(dlmTask, Mockito.times(1)).getJobType();
        Assert.assertEquals(JobType.ROLLBACK,dlmTask.getJobType());
    }
}