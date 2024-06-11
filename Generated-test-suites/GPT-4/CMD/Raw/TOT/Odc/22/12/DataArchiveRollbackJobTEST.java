import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.repository.impl.ScheduleTaskRepositoryImpl;

public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepositoryImpl mockedScheduleTaskRepository;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setup() {
        mockedScheduleTaskRepository = mock(ScheduleTaskRepositoryImpl.class);
        context = mock(JobExecutionContext.class);
        taskEntity = new ScheduleTaskEntity();
    }

    @Test
    public void executeDataArchiveTaskNotNullAndHasListTest() {

        DataArchiveRollbackJob abstractObj = new DataArchiveRollbackJob();
        abstractObj.setScheduleTaskRepository(mockedScheduleTaskRepository);

        when(context.getResult()).thenReturn(taskEntity);
        when(mockedScheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(mockedScheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(taskEntity);

        // call the method to test
        abstractObj.execute(context);
    }

    @Test(expected = NullPointerException.class)
    public void executeDataArchiveTaskIsNullTest() {

        DataArchiveRollbackJob abstractObj = new DataArchiveRollbackJob();
        abstractObj.setScheduleTaskRepository(mockedScheduleTaskRepository);

        when(context.getResult()).thenReturn(taskEntity);
        when(mockedScheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // call the method to test
        abstractObj.execute(context);
    }

    //continue writing test cases for the other methods in similar pattern
}