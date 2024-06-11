package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.TaskStatusUtils;

@Slf4j
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JsonUtils jsonUtils;
    private DlmJobIdUtil dlmJobIdUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonUtils = mock(JsonUtils.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dlmJobIdUtil = mock(DlmJobIdUtil.class);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = getDataArchiveRollbackParameters();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = getDataArchiveRollbackParameters();
        ScheduleTaskEntity dataArchiveTask = getDataArchiveTask();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(jsonUtils.fromJson(dataArchiveTask.getParametersJson(), DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);

        List<DlmTask> taskUnits = Arrays.asList(getDlmTask1(), getDlmTask2());
        when(jsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(taskUnits);

        doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), eq(taskUnits));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        TaskStatus taskStatus = TaskStatusUtils.getSuccessStatus();
        verify(scheduleTaskRepository).updateStatusById(dataArchiveTask.getId(), eq(taskStatus));
    }

    private DataArchiveRollbackParameters getDataArchiveRollbackParameters() {
        return new DataArchiveRollbackParameters();
    }

    private ScheduleTaskEntity getDataArchiveTask() {
        return new ScheduleTaskEntity();
    }

    private DlmTask getDlmTask1() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        return dlmTask;
    }

    private DlmTask getDlmTask2() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(2L);
        dlmTask.setStatus(TaskStatus.DONE);
        return dlmTask;
    }
}