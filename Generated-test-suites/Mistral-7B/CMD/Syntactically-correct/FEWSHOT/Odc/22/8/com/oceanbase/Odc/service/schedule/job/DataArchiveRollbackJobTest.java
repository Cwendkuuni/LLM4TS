package com.oceanbase.odc.service.schedule.job;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        taskEntity = new ScheduleTaskEntity();
        ObjectMapper objectMapper = new ObjectMapper();

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String json = objectMapper.writeValueAsString(rollbackParameters);
        taskEntity.setParametersJson(json);

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        List<DlmTask> taskUnits = new ArrayList<>();

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        String json = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"sourceDatabaseId\": 1,\n" +
                "        \"targetDatabaseId\": 2,\n" +
                "        \"jobType\": \"ROLLBACK\",\n" +
                "        \"status\": \"PREPARING\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"sourceDatabaseId\": 2,\n" +
                "        \"targetDatabaseId\": 1,\n" +
                "        \"jobType\": \"ROLLBACK\",\n" +
                "        \"status\": \"DONE\"\n" +
                "    }\n" +
                "]";
        dataArchiveTask.setResultJson(json);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        JsonNode jsonNode = JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<JsonNode>() {});
        List<DlmTask> dlmTasks = JsonUtils.fromJson(jsonNode, new TypeReference<List<DlmTask>>() {});

        taskUnits.addAll(dlmTasks);

        dataArchiveRollbackJob.execute(context);

        for (DlmTask dlmTask : taskUnits) {
            assertEquals(TaskStatus.DONE, dlmTask.getStatus());
            assertEquals(1L, dlmTask.getTargetDatabaseId());
            assertEquals(2L, dlmTask.getSourceDatabaseId());
            assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
        }

        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}