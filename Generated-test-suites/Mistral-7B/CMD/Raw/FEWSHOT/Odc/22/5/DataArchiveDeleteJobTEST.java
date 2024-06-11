package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String JOB_NAME = "testJobName";
    private static final String JOB_GROUP = "testJobGroup";
    private static final int ID = 1;
    private static final int DATA_ARCHIVE_TASK_ID = 1;
    private static final String RESULT_JSON = "{\"tasks\":[{\"id\":1,\"status\":\"PREPARING\"},{\"id\":2,\"status\":\"PREPARING\"}]}";
    private static final String PARAMETERS_JSON = "{\"dataArchiveTaskId\":1}";

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ObjectMapper objectMapper;

    @org.junit.Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            {
                setScheduleTaskRepository(scheduleTaskRepository);
            }
        };
    }

    @org.junit.Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(ID);
        taskEntity.setJobName(JOB_NAME);
        taskEntity.setJobGroup(JOB_GROUP);
        taskEntity.setParametersJson(PARAMETERS_JSON);

        when(context.getResult()).thenReturn(taskEntity);

        try {
            dataArchiveDeleteJob.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository).updateStatusById(ID, TaskStatus.FAILED);
    }

    @org.junit.Test
    public void testExecute_DataArchiveTaskNotFinished() {
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(ID);
        dataArchiveTask.setJobName(JOB_NAME);
        dataArchiveTask.setJobGroup(JOB_GROUP);
        dataArchiveTask.setParametersJson(PARAMETERS_JSON);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        when(context.getResult()).thenReturn(taskEntity);

        try {
            dataArchiveDeleteJob.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository).updateStatusById(ID, TaskStatus.FAILED);
    }

    @org.junit.Test
    public void testExecute_DataArchiveTaskFinished() {
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(ID);
        dataArchiveTask.setJobName(JOB_NAME);
        dataArchiveTask.setJobGroup(JOB_GROUP);
        dataArchiveTask.setParametersJson(PARAMETERS_JSON);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson( PARAMETERS_JSON, DataArchiveClearParameters.class );
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>(){})).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(ID, TaskStatus.SUCCESS);
    }

    @org.junit.Test
    public void testExecute_TasksPrepareFailed() {
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(ID);
        dataArchiveTask.setJobName(JOB_NAME);
        dataArchiveTask.setJobGroup(JOB_GROUP);
        dataArchiveTask.setParametersJson(PARAMETERS_JSON);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson( PARAMETERS_JSON, DataArchiveClearParameters.class );
        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());

        doThrow(new RuntimeException("Task preparation failed")).when(dataArchiveDeleteJob).executeTask(ID, taskUnits);

        try {
            dataArchiveDeleteJob.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository, never()).updateStatusById(ID, TaskStatus.SUCCESS);
    }

    @org.junit.Test
    public void testExecute_TasksPrepareSuccess() {
        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity()));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(ID);
        dataArchiveTask.setJobName(JOB_NAME);
        dataArchiveTask.setJobGroup(JOB_GROUP);
        dataArchiveTask.setParametersJson(PARAMETERS_JSON);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson( PARAMETERS_JSON, DataArchiveClearParameters.class );
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>(){})).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(ID, TaskStatus.SUCCESS);
    }
}