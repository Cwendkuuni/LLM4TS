package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(1L, TaskStatus.PREPARING, 1L, 2L));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(new DataArchiveRollbackParameters(1));

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.DONE, taskUnits.get(0).getStatus());
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
    }
}