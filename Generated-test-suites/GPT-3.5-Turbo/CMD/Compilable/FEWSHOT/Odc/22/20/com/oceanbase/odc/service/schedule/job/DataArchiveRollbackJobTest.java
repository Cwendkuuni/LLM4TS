package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
        ScheduleTaskEntity dataArchiveTask = dataArchiveTaskOption.get();

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert statements for the results
        assertNotNull(dataArchiveTask);
        assertTrue(dataArchiveTask.getStatus() == TaskStatus.FAILED);
        taskUnits.forEach(t -> {
            Long temp = t.getSourceDatabaseId();
            assertEquals(temp, t.getTargetDatabaseId());
            assertEquals(JobType.ROLLBACK, t.getJobType());
            assertEquals(TaskStatus.DONE, t.getStatus());
        });
        assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
    }
}