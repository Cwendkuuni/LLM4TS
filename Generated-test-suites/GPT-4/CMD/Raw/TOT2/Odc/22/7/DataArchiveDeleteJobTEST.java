
import static org.mockito.Mockito.*;

// imports here

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        JobExecutionContext contextMock =getMock(JobExecutionContext.class);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(contextMock);
        // verification logic goes here
    }

    @Test
    public void testExecute_withNullContext() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(null);
        // verification logic goes here
    }

    @Test
    public void testExecute_withEmptyDataArchiveTaskOption() {
        JobExecutionContext contextMock =getMock(JobExecutionContext.class);
        when(contextMock.getResult()).thenReturn(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(contextMock);
        // verification logic goes here
    }

    @Test(expected = JsonParseException.class)
    public void testExecute_withJsonParseException() {
        JobExecutionContext contextMock =getMock(JobExecutionContext.class);
        ScheduleTaskEntity entity = new ScheduleTaskEntity();
        entity.setParametersJson("invalid_json");
        when(contextMock.getResult()).thenReturn(entity);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(contextMock);
        // no verification necessary because we're expecting an exception
    }
}
