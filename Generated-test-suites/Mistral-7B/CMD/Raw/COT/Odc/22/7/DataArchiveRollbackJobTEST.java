package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockObject;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobRepository jobRepository = Mockito.mock(JobRepository.class);
    private StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setJobRepository(jobRepository);
    }

    @Test
    public void testExecute_TaskNotFound() {
        // Given
        MockObject context = new MockObject(JobExecutionContext.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-existing-id");

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(jobRepository).updateStatusById("non-existing-id", TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskFound() {
        // Given
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");
        Mockito.when(jobRepository.findById("existing-id")).thenReturn(Optional.of(taskEntity));

        MockObject context = new MockObject(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        Mockito.verify(jobRepository).updateStatusById("existing-id", TaskStatus.DONE);
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_NullTaskUnits() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");
        Mockito.when(jobRepository.findById("existing-id")).thenReturn(Optional.of(taskEntity));

        MockObject context = new MockObject(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        // When
        dataArchiveRollbackJob.execute(context);
        dataArchiveRollbackJob.executeTask(taskEntity.getId(), null);

        // Then
        // This test case should throw an IllegalStateException
    }

    @Test
    public void testExecute_TaskUnitsPreparing() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");
        Mockito.when(jobRepository.findById("existing-id")).thenReturn(Optional.of(taskEntity));

        MockObject context = new MockObject(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        Mockito.when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.PREPARING);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(jobRepository).updateStatusById("existing-id", TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskUnitsDone() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existing-id");
        Mockito.when(jobRepository.findById("existing-id")).thenReturn(Optional.of(taskEntity));

        MockObject context = new MockObject(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(taskEntity);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        Mockito.when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.DONE);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(jobRepository).updateStatusById("existing-id", TaskStatus.DONE);
    }
}