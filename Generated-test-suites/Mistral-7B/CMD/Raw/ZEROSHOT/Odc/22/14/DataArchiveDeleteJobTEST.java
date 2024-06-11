import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_InvalidTaskNotFound() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID().toString());

        when(scheduleTaskRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // when then
        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class, new Object() {}.getClass()));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_InvalidTaskStatus() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID().toString());

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(UUID.randomUUID().toString());
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // when then
        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class, new Object() {}.getClass()));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_ValidTask() throws JobExecutionException {
        // given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID().toString());

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(UUID.randomUUID().toString());
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(UUID.randomUUID().toString());
        dlmTasks.add(dlmTask);

        String jsonString = objectMapper.writeValueAsString(dlmTasks);
        when(scheduleTaskRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(Mockito.anyString(), Mockito.any()))
                .thenReturn(true);
        when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);

        // when then
        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class, new Object() {}.getClass()));

        // then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.RUNNING));
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.FINISHED));
    }
}