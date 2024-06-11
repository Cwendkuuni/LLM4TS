import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        context = mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(taskUnits));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), isA(TaskStatus.class));
        verify(dataArchiveRollbackJob).executeTask(taskEntity.getId(), taskUnits);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        TaskStatus expected = TaskStatus.DONE;
        assertEquals(expected, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }
}