package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        TaskDAO taskDAO = Mockito.mock(TaskDAO.class);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob(taskDAO);
        DataArchiveClearParameters params = new DataArchiveClearParameters();

        params.setDataArchiveTaskId("12345");
        taskEntity.setParametersJson(new ObjectMapper().writeValueAsString(params));
        taskEntity.setId("23456");
        taskEntity.setResultJson("[]");

        Optional<ScheduleTaskEntity> taskOption = Optional.of(taskEntity);

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskDAO.findById(Mockito.anyString())).thenReturn(taskOption);

        job.execute(context);

        Mockito.verify(taskDAO, Mockito.times(1)).findById("12345");
        Mockito.verify(taskDAO, Mockito.times(1)).updateStatusById(Mockito.eq("23456"), Mockito.any(TaskStatus.class));
    }

    @Test
    public void testExecuteWithTasks() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        TaskDAO taskDAO = Mockito.mock(TaskDAO.class);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob(taskDAO);
        DataArchiveClearParameters params = new DataArchiveClearParameters();

        params.setDataArchiveTaskId("12345");
        taskEntity.setParametersJson(new ObjectMapper().writeValueAsString(params));
        taskEntity.setId("23456");

        List<DlmTask> tasks = new ArrayList<>();
        DlmTask task = new DlmTask();
        task.setId("34567");
        task.setStatus(TaskStatus.PREPARING);
        tasks.add(task);

        taskEntity.setResultJson("[{\"id\":\"34567\",\"status\":\"PREPARING\"}]");

        Optional<ScheduleTaskEntity> taskOption = Optional.of(taskEntity);

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskDAO.findById(Mockito.anyString())).thenReturn(taskOption);

        job.execute(context);

        Mockito.verify(taskDAO, Mockito.times(1)).findById("12345");
        Mockito.verify(taskDAO, Mockito.times(1)).updateStatusById(Mockito.eq("23456"), Mockito.any(TaskStatus.class));
    }
}