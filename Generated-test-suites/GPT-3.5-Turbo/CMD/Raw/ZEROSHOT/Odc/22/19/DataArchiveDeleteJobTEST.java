package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockContext;
    
    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("12345");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        taskUnits.add(dlmTask);
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        
        DataArchiveDeleteJob spyJob = Mockito.spy(dataArchiveDeleteJob);
        Mockito.when(spyJob.scheduleTaskRepository.findById("12345")).thenReturn(Optional.of(dataArchiveTask));

        spyJob.execute(mockContext);

        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }
}