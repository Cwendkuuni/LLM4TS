package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("notFoundId");

        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        job.execute(context);

        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
        verify(log, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId={}",
                "notFoundId");
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("foundId");

        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getResultJson()).thenReturn(createJsonString(new ArrayList<DlmTask>()));

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        ObjectMapper mapper = new ObjectMapper();
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        when(taskEntity.getParametersJson()).thenReturn(mapper.writeValueAsString(rollbackParameters));

        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
    }

    private String createJsonString(List<DlmTask> dlmTasks) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(dlmTasks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}