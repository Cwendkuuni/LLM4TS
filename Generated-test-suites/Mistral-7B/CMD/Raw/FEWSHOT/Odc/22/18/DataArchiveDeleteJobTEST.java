import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.JobType.DELETE;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.FAILED;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.PREPARING;
import static com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.TaskStatus.DONE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> dlmTasks;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

    @Before
    public void setup() {
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson("{}");
        scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setParametersJson("{}");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");

        dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(PREPARING);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(scheduleTaskEntity));
        when(scheduleTaskRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(FAILED))).thenReturn(true);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(null);
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_dataArchiveTaskNotFinished() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext() {});
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_prepareTasksForClear() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext() {
            @Override
            public Object getMergedJobDataMap() {
                return scheduleTaskEntity;
            }
        });

        assertEquals(TaskStatus.PREPARING, dlmTasks.get(0).getStatus());
        assertEquals(DELETE, dlmTasks.get(0).getJobType());
        assertEquals(1, dlmTasks.size());
    }

    @Test
    public void testExecute_updateTaskStatus() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext() {
            @Override
            public Object getMergedJobDataMap() {
                return scheduleTaskEntity;
            }
        });

        assertTrue(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING));
    }

    @Test
    public void testExecute_rollbackFailed() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext() {
            @Override
            public Object getMergedJobDataMap() {
                return scheduleTaskEntity;
            }
        });

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_updateTaskStatusSuccess() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(new JobExecutionContext() {
            @Override
            public Object getMergedJobDataMap() {
                return scheduleTaskEntity;
            }
        });

        assertTrue(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), DONE));
    }
}