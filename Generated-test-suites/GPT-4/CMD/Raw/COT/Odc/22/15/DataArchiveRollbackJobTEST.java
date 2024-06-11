import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.Optional;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        try {
            dataArchiveRollbackJob.execute(context);
        } catch (MockitoException me) {
            // Verify that methods are being invoked in Mockito stubs                     
            verify(context, atLeastOnce()).getResult();
            verify(scheduleTaskRepository, atLeastOnce()).findById(any());
        }
    }
}