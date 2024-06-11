package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Test
    public void testExecute() {
        // Setup
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());

        try {
            // Run the test
            dataArchiveDeleteJob.execute(jobExecutionContext);

            // Verify the results
        } catch (JobExecutionException e) {
            // Handle exception here, because a Method could throw an exception
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute_ScheduleTaskEntityNotFound() {
        // Setup
        when(jobExecutionContext.getResult()).thenReturn(null);

        try {
            // Run the test
            dataArchiveDeleteJob.execute(jobExecutionContext);

            // Verify the results
        } catch (JobExecutionException e) {
            // Handle exception here, because a Method could throw an exception
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute_ScheduleTaskEntityIsPresentButStatusNotDone() {
        // Setup
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        Optional<ScheduleTaskEntity> optional = Optional.of(scheduleTaskEntity);
        doReturn(optional).when(dataArchiveDeleteJob).findDataArchiveTask(scheduleTaskEntity);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        try {
            // Run the test
            dataArchiveDeleteJob.execute(jobExecutionContext);

            // Verify the results
        } catch (JobExecutionException e) {
            // Handle exception here, because a Method could throw an exception
            e.printStackTrace();
        }
    }
}
