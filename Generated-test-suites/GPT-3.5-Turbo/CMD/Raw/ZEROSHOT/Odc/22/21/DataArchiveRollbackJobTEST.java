import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity mockTaskEntity;
    private DataArchiveRollbackParameters mockRollbackParameters;
    private ScheduleTaskEntity mockDataArchiveTask;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();

        mockTaskEntity = mock(ScheduleTaskEntity.class);
        mockRollbackParameters = new DataArchiveRollbackParameters();
        mockDataArchiveTask = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute_Success() {
        // Arrange
        when(mockTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(mockRollbackParameters));
        when(mockDataArchiveTask.getResultJson()).thenReturn("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"ARCHIVE\",\"status\":\"DONE\"}]");
        when(mockDataArchiveTask.getId()).thenReturn(1L);
        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(mockDataArchiveTask.isPresent()).thenReturn(true);

        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        when(mockContext.getResult()).thenReturn(mockTaskEntity);

        // Act
        dataArchiveRollbackJob.execute(mockContext);

        // Assert
        verify(mockTaskEntity, times(2)).getId();
        verify(mockTaskEntity).getParametersJson();
        verify(mockDataArchiveTask).getResultJson();
        verify(mockDataArchiveTask).getId();
        verify(mockDataArchiveTask).getStatus();
        verify(mockDataArchiveTask).isPresent();
    }

    @Test
    public void testExecute_Failure() {
        // Arrange
        when(mockTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(mockRollbackParameters));
        when(mockTaskEntity.getId()).thenReturn(1L);
        when(mockTaskEntity.isPresent()).thenReturn(false);

        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        when(mockContext.getResult()).thenReturn(mockTaskEntity);

        // Act
        dataArchiveRollbackJob.execute(mockContext);

        // Assert
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
        verify(mockTaskEntity, times(2)).getId();
        verify(mockTaskEntity).getParametersJson();
        verify(mockDataArchiveTask, never()).getResultJson();
        verify(mockDataArchiveTask, never()).getId();
        verify(mockDataArchiveTask, never()).getStatus();
        verify(mockDataArchiveTask, never()).isPresent();
    }
}