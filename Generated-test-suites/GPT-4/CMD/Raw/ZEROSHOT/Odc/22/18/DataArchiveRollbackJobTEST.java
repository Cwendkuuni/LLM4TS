
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DlmTask dlmTask;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        List<DlmTask> tasks = new ArrayList<>();
        tasks.add(dlmTask);
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class))).thenReturn(tasks);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("123");
        when(scheduleTaskRepository.findById(any(String.class))).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(String.class), any(TaskStatus.class));
    }

    @Test
    public void testExecuteWithNoDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn("123");
        when(scheduleTaskRepository.findById(any(String.class))).thenReturn(Optional.empty());
        
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(String.class), TaskStatus.FAILED);
    }
}
