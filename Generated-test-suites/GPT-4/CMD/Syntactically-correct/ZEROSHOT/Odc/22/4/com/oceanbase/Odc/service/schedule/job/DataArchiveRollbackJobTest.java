package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.ofNullable(dataArchiveTask));

        
        List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        when(DlmJobIdUtil.generateHistoryJobId(anyString(),anyString(),anyLong(),anyInt())).thenReturn("testJobId");
        when(DlmTask.getSourceDatabaseId()).thenReturn(anyLong());
        when(DlmTask.getTargetDatabaseId()).thenReturn(anyLong());
        when(DlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @After
    public void tearDown() {
        dataArchiveRollbackJob = null;
    }
}
