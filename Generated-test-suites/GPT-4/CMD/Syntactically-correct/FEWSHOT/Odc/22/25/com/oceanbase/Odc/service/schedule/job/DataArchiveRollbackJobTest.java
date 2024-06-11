
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;

public class DataArchiveRollbackJobTest {

    @Mock JobExecutionContext context;
    @Mock ScheduleTaskEntity taskEntity;
    @Mock DataArchiveRollbackParameters rollbackParameters;
    @Mock ScheduleTaskEntity taskEntityArchive;
    @Mock DlmTask taskUnit;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDataArchiveRollbackJob_executeMethod() {
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(taskUnit);
        Optional<ScheduleTaskEntity> taskEntityOption = Optional.of(taskEntityArchive);
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

        // Mocking
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(taskEntityOption);
        when(dataArchiveTaskOption.get()).thenReturn(taskEntityArchive);
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        // Execute method
        dataArchiveRollbackJob.execute(context);

        //Assertions and verifications
        verify(scheduleTaskRepository).findById(any());
        verify(taskUnit).setId(any());
        verify(taskUnit).setSourceDatabaseId(any());
        verify(taskUnit).setTargetDatabaseId(any());
        verify(taskUnit).setJobType(JobType.ROLLBACK);
        verify(taskUnit).setStatus(TaskStatus.DONE);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

}