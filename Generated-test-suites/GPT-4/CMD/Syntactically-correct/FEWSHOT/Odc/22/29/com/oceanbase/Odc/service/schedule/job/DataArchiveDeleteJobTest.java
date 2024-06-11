package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

/**
 * @Author：testWriter
 * @Date: 2023/6/14 09:51
 */
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private DataArchiveClearParameters parameters;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);
    }

    @Test
    public void testExecute_NoArchiveTask() {
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_ArchiveTaskExists() {
        ScheduleTaskEntity archiveTask = new ScheduleTaskEntity();
        archiveTask.setStatus(TaskStatus.DONE);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        Mockito.when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(archiveTask));

        DlmTask dlmTask = new DlmTask();
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);
        archiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        dataArchiveDeleteJob.execute(context);

        // Verify that the task status is updated based on the task results
        Mockito.verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), any(TaskStatus.class));
    }
}