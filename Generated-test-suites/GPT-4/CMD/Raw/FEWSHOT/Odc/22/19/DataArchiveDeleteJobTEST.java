/*
 * Here are the JUnit 4 test cases for the DataArchiveDeleteJob class.
 * These test cases check two main scenarios: 
 *     1. When the archive task is not present or not done 
 *     2. When the archive task is both present and marked as "done"
 */
package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecuteWhenArchiveTaskNotPresent() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("testId");
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("testArchiveTaskId");
        scheduleTaskEntity.setParametersJson(params.toString());
        
        doReturn(scheduleTaskEntity).when(context).getResult();
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById("testArchiveTaskId");

        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository).updateStatusById("testId", TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenArchiveTaskNotDone() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("testId");
        ScheduleTaskEntity archiveTaskEntity = new ScheduleTaskEntity();
        archiveTaskEntity.setStatus(TaskStatus.RUNNING);
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("testArchiveTaskId");
        scheduleTaskEntity.setParametersJson(params.toString());
        
        doReturn(scheduleTaskEntity).when(context).getResult();
        doReturn(Optional.of(archiveTaskEntity)).when(scheduleTaskRepository).findById("testArchiveTaskId");

        dataArchiveDeleteJob.execute(context);

        Mockito.verify(scheduleTaskRepository).updateStatusById("testId", TaskStatus.FAILED);
    }

    @Test
    public void testExecuteWhenArchiveTaskDone() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId("testId");
        ScheduleTaskEntity archiveTaskEntity = new ScheduleTaskEntity();
        archiveTaskEntity.setStatus(TaskStatus.DONE);
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("testArchiveTaskId");
        scheduleTaskEntity.setParametersJson(params.toString());
        
        doReturn(scheduleTaskEntity).when(context).getResult();
        doReturn(Optional.of(archiveTaskEntity)).when(scheduleTaskRepository).findById("testArchiveTaskId");

        dataArchiveDeleteJob.execute(context);

        Mockito.verify(dataArchiveDeleteJob).executeTask("testId", Arrays.asList());
        Mockito.verify(scheduleTaskRepository).updateStatusById("testId", archiveTaskEntity.getStatus());
    }
}