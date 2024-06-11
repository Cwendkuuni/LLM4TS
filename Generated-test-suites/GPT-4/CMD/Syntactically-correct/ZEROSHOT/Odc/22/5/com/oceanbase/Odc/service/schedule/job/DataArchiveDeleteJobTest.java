
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
 
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockContext;
    @Mock
    private ScheduleTaskEntity taskEntityMock;
    @Mock
    private Optional<ScheduleTaskEntity> optionalScheduleTaskMock;
    @Mock
    private ScheduleTaskEntity dataArchiveTaskMock;
    private DataArchiveDeleteJob dataArchiveDeleteJob;
 
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }
 
    @Test
    public void executeTest() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("archTaskId1");

        when(mockContext.getResult()).thenReturn(taskEntityMock);
        when(taskEntityMock.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntityMock.getId()).thenReturn("taskId1");
        when(scheduleTaskRepository.findById("archTaskId1")).thenReturn(optionalScheduleTaskMock);
        when(optionalScheduleTaskMock.isPresent()).thenReturn(true);
        when(optionalScheduleTaskMock.get()).thenReturn(dataArchiveTaskMock);
        when(dataArchiveTaskMock.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTaskMock.getResultJson()).thenReturn("[{\"id\":\"id1\",\"jobType\":\"archJobType\",\"status\":\"PREPARING\"}]");
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), any(), anyInt())).thenReturn("generatedId");
        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());
        when(getTaskStatus(dlmTasks)).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(mockContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntityMock.getId(), TaskStatus.DONE);
    }
    
    @Test
    public void executeTest_ArchiveTaskNotPresent() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("archTaskId2");

        when(mockContext.getResult()).thenReturn(taskEntityMock);
        when(taskEntityMock.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntityMock.getId()).thenReturn("taskId2");
        when(scheduleTaskRepository.findById("archTaskId2")).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(mockContext);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntityMock.getId(), TaskStatus.FAILED);
    }
    
    @Test
    public void executeTest_ArchiveTaskNotDone() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId("archTaskId3");

        when(mockContext.getResult()).thenReturn(taskEntityMock);
        when(taskEntityMock.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntityMock.getId()).thenReturn("taskId3");
        when(scheduleTaskRepository.findById("archTaskId3")).thenReturn(optionalScheduleTaskMock);
        when(optionalScheduleTaskMock.isPresent()).thenReturn(true);
        when(optionalScheduleTaskMock.get()).thenReturn(dataArchiveTaskMock);
        when(dataArchiveTaskMock.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveDeleteJob.execute(mockContext);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntityMock.getId(), TaskStatus.FAILED);
    }
}
