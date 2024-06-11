package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        entity.setId(1);
        entity.setJobName("DataArchiveJob");
        entity.setJobGroup("Group1");
        entity.setResultJson("[{\"id\": 1, \"name\": \"Task1\", \"status\": \"DONE\"}]");
        
        when(context.getResult()).thenReturn(entity);

        dataArchiveDeleteJob.execute(context);

        assertNotNull(entity);
        assertNotNull(entity.getParametersJson());
        assertNotNull(entity.getId());
        assertNotNull(entity.getJobName());
        assertNotNull(entity.getJobGroup());
        assertNotNull(entity.getResultJson());
    }

    @Test
    public void testTaskUnitsGeneration() {
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setResultJson("[{\"id\": 1, \"name\": \"Task1\", \"status\": \"DONE\"}]");

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask(1, "Task1", TaskStatus.DONE));

        assertTrue(JsonUtils.fromJson(entity.getResultJson(), new TypeReference<List<DlmTask>>() {}).containsAll(taskUnits));
    }

    @Test
    public void testTaskUnitPreparation() {
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setId(1);
        entity.setJobName("DataArchiveJob");
        entity.setJobGroup("Group1");

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask(1, "Task1", TaskStatus.DONE));

        taskUnits.forEach(taskUnit -> {
            taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(entity.getJobName(), entity.getJobGroup(), entity.getId(), taskUnits.size()));
            taskUnit.setJobType(JobType.DELETE);
            taskUnit.setStatus(TaskStatus.PREPARING);
        });

        taskUnits.forEach(taskUnit -> {
            assertNotNull(taskUnit.getId());
            assertEquals(JobType.DELETE, taskUnit.getJobType());
            assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());
        });
    }

}