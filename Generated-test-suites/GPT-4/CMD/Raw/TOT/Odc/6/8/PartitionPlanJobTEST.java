import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext jobExecutionContextMock;

    @Mock
    private ScheduleService scheduleServiceMock;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);
    }

    @Test
    public void testExecute() {
        try {
            Long scheduleId = 1L;
            when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
            partitionPlanJob.execute(jobExecutionContextMock);
        } catch (Exception e) {
            fail("Expected no exceptions, but got: " + e.getMessage());
        }
    }

    @Test(expected=UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}