package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-existing-id");

        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity());
        dataArchiveRollbackJob.execute(context);

        // then
        assertEquals(TaskStatus.FAILED, getLastTaskStatus(dataArchiveRollbackJob.jobThread));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setResultJson("{\"tasks\":[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2},{\"id\":2,\"sourceDatabaseId\":2,\"targetDatabaseId\":3}]}");
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity());
        dataArchiveRollbackJob.execute(context);

        // then
        List<DlmTask> tasks = parseTasksFromJson("{\"tasks\":[{\"id\":1,\"sourceDatabaseId\":3,\"targetDatabaseId\":2},{\"id\":2,\"sourceDatabaseId\":2,\"targetDatabaseId\":3}]}");
        assertEquals(TaskStatus.DONE, getLastTaskStatus(dataArchiveRollbackJob.jobThread));
        assertEquals(tasks, dataArchiveRollbackJob.getExecutedTasks());
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_TaskExecutionException() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("existing-id");
        dataArchiveTask.setResultJson("{\"tasks\":[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]}");
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // when - exception
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new JobDataMap());
        context.setResult(new ScheduleTaskEntity());
        try {
            dataArchiveRollbackJob.execute(context);
        } catch (Exception e) {
            throw new JobExecutionException("Exception during task execution", e);
        }

        // then
        assertEquals(TaskStatus.FAILED, getLastTaskStatus(dataArchiveRollbackJob.jobThread));
    }

    private List<DlmTask> parseTasksFromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        List<DlmTask> tasks = new ArrayList<>();
        for (JsonNode taskNode : rootNode.path("tasks")) {
            DlmTask dlmTask = new DlmTask();
            parseDlmTaskFromJson(dlmTask, taskNode);
            tasks.add(dlmTask);
        }
        return tasks;
    }

    private void parseDlmTaskFromJson(DlmTask dlmTask, JsonNode jsonNode) {
        dlmTask.setId(jsonNode.get("id").asLong());
        dlmTask.setSourceDatabaseId(jsonNode.get("sourceDatabaseId").asLong());
        dlmTask.setTargetDatabaseId(jsonNode.get("targetDatabaseId").asLong());
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
    }

    private TaskStatus getLastTaskStatus(Thread thread) {
        DataArchiveRollbackJob dataArchiveRollbackJob = (DataArchiveRollbackJob) thread.getCurrentFrame().getLocalVariables()[0];
        return dataArchiveRollbackJob.getLastTaskStatus();
    }
}