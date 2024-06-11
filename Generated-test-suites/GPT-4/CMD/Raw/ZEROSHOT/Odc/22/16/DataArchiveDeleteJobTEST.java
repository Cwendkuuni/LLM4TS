
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;
    @Mock
    private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;
    @Mock
    private List<DlmTask> taskUnits;

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;
    

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"123456\"}");
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(dataArchiveTaskOption);
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[]");
    }

    @Test
    public void testExecuteDataArchiveTaskNotPresent() {
        when(dataArchiveTaskOption.isPresent()).thenReturn(false);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PAUSED);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWithTasks() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = mock(DlmTask.class);
        taskUnits.add(taskUnit);

        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        verify(taskUnit, times(1)).setId(anyString());
        verify(taskUnit, times(1)).setJobType(JobType.DELETE);
        verify(taskUnit, times(1)).setStatus(TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.PREPARING));
    }
}
