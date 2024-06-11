package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(10L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(10L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(10L)).thenReturn(java.util.Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        assertNotNull(dataArchiveDeleteJob.getJobThread());
        assertNotNull(dataArchiveDeleteJob.getScheduleTaskRepository());

        // Add more assertions based on the actual behavior of the execute method
    }
}