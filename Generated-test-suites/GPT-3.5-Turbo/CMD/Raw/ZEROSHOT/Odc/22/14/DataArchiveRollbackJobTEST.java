import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    JobExecutionContext jobExecutionContextMock;
    @Mock
    ScheduleTaskEntity scheduleTaskEntityMock;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntityMock.getParametersJson()).thenReturn(parametersJson);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntityMock));
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        
        String resultJson = JsonUtils.toJson(taskUnits);
        when(scheduleTaskEntityMock.getResultJson()).thenReturn(resultJson);
        when(scheduleTaskEntityMock.getId()).thenReturn(1L);
        
        dataArchiveRollbackJob.execute(jobExecutionContextMock);
        
        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
    }

}