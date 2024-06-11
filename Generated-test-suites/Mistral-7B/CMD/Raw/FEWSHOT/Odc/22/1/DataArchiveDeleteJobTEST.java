package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
import com.oceanbase.tools.migrator.common.utils.JsonUtilsTest;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext jobExecutionContext;
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        scheduleTaskEntity.setStatus(TaskStatus.NEW);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
            assertFalse("Expected an exception to be thrown", true);
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
            assertFalse("Expected an exception to be thrown", true);
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
    }

    @Test
    public void testExecute_JsonParsingException() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenThrow(new Exception());

        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
            assertFalse("Expected an exception to be thrown", true);
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }
}

class DataArchiveTaskEntity {
    private ScheduleTaskEntity scheduleTaskEntity;

    public ScheduleTaskEntity getScheduleTaskEntity() {
        return scheduleTaskEntity;
    }

    public void setScheduleTaskEntity(ScheduleTaskEntity scheduleTaskEntity) {
        this.scheduleTaskEntity = scheduleTaskEntity;
    }
}