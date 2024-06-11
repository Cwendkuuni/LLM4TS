import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    JobExecutionContext jobExecutionContextMock;

    @Mock
    ScheduleTaskEntity scheduleTaskEntityMock;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void executeTest() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.NORMAL);
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskEntityMock.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(scheduleTaskEntityMock.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
        when(jobExecutionContextMock.getResult()).thenReturn(scheduleTaskEntityMock);

        dataArchiveRollbackJob.execute(jobExecutionContextMock);

        verify(scheduleTaskEntityMock, times(1)).getParametersJson();
        verify(scheduleTaskEntityMock, times(1)).getResultJson();
        assertEquals(TaskStatus.DONE, dlmTask.getStatus());
    }
}