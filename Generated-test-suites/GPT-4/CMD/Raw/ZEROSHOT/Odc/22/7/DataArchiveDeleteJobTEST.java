
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        dataArchiveDeleteJob = Mockito.spy(dataArchiveDeleteJob);

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1);
        Mockito.doReturn(scheduleTaskEntity).when(context).getMergedJobDataMap();

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2);

        Mockito.doReturn(Optional.of(new ScheduleTaskEntity()))
                .when(dataArchiveDeleteJob.scheduleTaskRepository)
                .findById(any(int.class));

        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTask.setId(DlmJobIdUtil.generateHistoryJobId(String.valueOf(scheduleTaskEntity.getId()), "JobGroup", String.valueOf(scheduleTaskEntity.getId()),1));
        dlmTask.setJobType(JobType.DELETE);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);

        Mockito.doReturn(taskUnits)
                .when(dataArchiveDeleteJob.JsonUtils)
                .fromJson(any(String.class), 
                           any(TypeReference.class));     

        dataArchiveDeleteJob.execute(context);
    }
}
