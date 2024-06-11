
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {
    
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private ScheduleTaskEntity archiveTaskEntity;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private DlmTask taskUnit;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("");
        when(scheduleTaskEntity.getId()).thenReturn(123L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(archiveTaskEntity));
        when(archiveTaskEntity.getResultJson()).thenReturn("");
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(taskUnit);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(TaskStatus.PREPARING);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);
        // Test if it correctly sets TaskStatus to FAILED when DataArchiveTask is not found
        Assert.assertEquals(scheduleTaskRepository.updateStatusById(anyLong(), any()), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteSuccessfulRollback() {
        dataArchiveRollbackJob.execute(context);
        // Test successful execution
        Assert.assertEquals(scheduleTaskRepository.updateStatusById(anyLong(), any()), TaskStatus.PREPARING);
    }

    @Test(expected = SqlExceptionHelper.class)
    public void testExecuteSqlException() {
        when(scheduleTaskRepository.findById(anyLong())).thenThrow(SqlExceptionHelper.class);
        dataArchiveRollbackJob.execute(context);
    }
}
