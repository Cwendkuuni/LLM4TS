
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;
    @Mock
    private DlmTask dlmTask;
    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private String parametersJson = "{\"dataArchiveTaskId\":1}";
    private String resultJson = "[{\"jobType\":\"DELETE\",\"status\":\"PREPARING\"}]";
    private DataArchiveClearParameters dataArchiveClearParameters;
    private long taskId = 1L;

    @Before
    public void setUp() throws Exception {
        dataArchiveClearParameters = JsonUtils.fromJson(parametersJson, DataArchiveClearParameters.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(dlmTask.getId()).thenReturn(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                taskEntity.getId(),
                Arrays.asList(dlmTask).size()));
    }

    @Test
    public void testExecute_GivenTaskDoesNotExist_ShouldSetStatusToFailed() {
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(taskId)).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_GivenTaskExistsButNotDone_ShouldSetStatusToFailed() {
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(dataArchiveDeleteJob.scheduleTaskRepository).updateStatusById(taskId, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_GivenTaskExistsAndDone_ShouldExecuteTask() {
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(taskId)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(dataArchiveDeleteJob).executeTask(taskId, Arrays.asList(dlmTask));
    }
}
