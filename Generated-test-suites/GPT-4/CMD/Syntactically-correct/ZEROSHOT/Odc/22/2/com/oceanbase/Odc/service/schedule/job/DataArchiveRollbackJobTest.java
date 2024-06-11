package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext mockedContext;
    
    @Mock
    ScheduleTaskEntity mockedScheduleTaskEntity;

    @Mock
    DataArchiveRollbackParameters mockParams;

    @Mock
    DlmTask mockTask;

    List<DlmTask> mockTasks = Arrays.asList(mockTask, mockTask);

    DataArchiveRollbackJob job;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        job = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecuteWhenTaskNotFound() {
        when(mockedContext.getResult()).thenReturn(mockedScheduleTaskEntity);
        when(mockedScheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"1234\"}");
        when(mockParams.getDataArchiveTaskId()).thenReturn("");
        job.execute(mockedContext);
        verify(mockedScheduleTaskEntity, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteWhenTaskFound() {
        when(mockedContext.getResult()).thenReturn(mockedScheduleTaskEntity);
        when(mockedScheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"1234\"}");
        when(mockParams.getDataArchiveTaskId()).thenReturn("1234");
        when(mockedScheduleTaskEntity.getResultJson()).thenReturn("[{\"targetDatabaseId\":\"1\"}]");
        job.execute(mockedContext);
        verify(mockedScheduleTaskEntity, times(1)).updateStatusById(anyString(), any());
    }

    @Test
    public void testExecuteWhenTasksCreated() {
        when(mockedContext.getResult()).thenReturn(mockedScheduleTaskEntity);
        when(mockedScheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"1234\"}");
        when(mockParams.getDataArchiveTaskId()).thenReturn("1234");
        when(mockedScheduleTaskEntity.getResultJson()).thenReturn("[{\"targetDatabaseId\":\"1\"},{\"targetDatabaseId\":\"1\"}]");
        job.execute(mockedContext);
        verify(mockedScheduleTaskEntity, times(1)).updateStatusById(anyString(), any());
    }
}
