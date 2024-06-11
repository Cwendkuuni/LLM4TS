package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.core.shared.constant.TaskStatus.FAILED;
import static com.oceanbase.odc.core.shared.constant.TaskStatus.PREPARING;
import static com.oceanbase.odc.core.shared.constant.TaskStatus.DONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Before
    public void setUp() {
        taskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED))).thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.DONE))).thenReturn(1);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        dataArchiveDeleteJob.execute(null);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setStatus(TaskStatus.RUNNING);
        dataArchiveDeleteJob.execute(null);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_EmptyDataArchiveTasksList() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        dataArchiveDeleteJob.execute(null);
        verify(scheduleTaskRepository, never()).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_DataArchiveTasksListNotEmpty() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));
        DlmTask resultDlmTask = new DlmTask();
        resultDlmTask.setId(2L);
        resultDlmTask.setStatus(TaskStatus.PREPARING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED)))
                .thenReturn(1);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE)))
                .thenReturn(1);
        when(executeTask(anyLong(), eq(taskUnits)))
                .thenReturn(new ArrayList<DlmTask>() {{
                    add(resultDlmTask);
                }});
        DataArchiveClearParameters resultDataArchiveClearParameters = JsonUtils.fromJson(
                dataArchiveDeleteJob.execute(null).getParametersJson(), DataArchiveClearParameters.class);
        assertEquals(dataArchiveClearParameters.getDataArchiveTaskId(), resultDataArchiveClearParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
    }
}