import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private static final String JOB_NAME = "DataArchiveDeleteJob";
 private static final String JOB_GROUP = "DataArchiveDeleteJobGroup";
 private static final String PARAMETERS_JSON = "{\"dataArchiveTaskId\":1}";
 private static final String RESULT_JSON = "[]";
 private static final int DATA_ARCHIVE_TASK_ID = 1;
 private static final int NUM_OF_TASK_UNIT = 5;
 private static final String TASK_ID_SUFFIX = "_history";
 private static final JobType JOB_TYPE = JobType.DELETE;
 private static final TaskStatus TASK_STATUS_DONE = TaskStatus.DONE;
 private static final TaskStatus TASK_STATUS_FAILED = TaskStatus.FAILED;
 private static final TaskStatus TASK_STATUS_PREPARING = TaskStatus.PREPARING;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobExecutionContext context;
 private Trigger trigger;
 private Scheduler scheduler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 context = new MockitoJobBuilder(DataArchiveDeleteJob.class)
 .withIdentity(JOB_NAME, JOB_GROUP)
 .usingJobData("parametersJson", PARAMETERS_JSON)
 .build();
 trigger = newTrigger()
 .withIdentity("triggerName", "triggerGroup")
 .startNow();
 try {
 scheduler = new StdSchedulerFactory().getScheduler();
 scheduler.start();
 } catch (SchedulerException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testExecute_TaskNotFound() throws SchedulerException, JobExecutionException {
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TASK_STATUS_FAILED));
 }

 @Test
 public void testExecute_TaskNotDone() throws SchedulerException, JobExecutionException {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTask.setStatus(TASK_STATUS_DONE);

 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TASK_STATUS_FAILED));
 }

 @Test
 public void testExecute_Success() throws SchedulerException, JobExecutionException {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(DATA_ARCHIVE_TASK_ID);
 dataArchiveTask.setStatus(TASK_STATUS_DONE);
 dataArchiveTask.setResultJson(RESULT_JSON);

 List<DlmTask> taskUnits = new ArrayList<>();
 for (int i = 0; i < NUM_OF_TASK_UNIT; i++) {
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(i + 1);
 dlmTask.setJobType(JOB_TYPE);
 dlmTask.setStatus(TASK_STATUS_PREPARING);
 taskUnits.add(dlmTask);
 }

 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TASK_STATUS_DONE));

 List<DlmTask> taskUnitsFromDb = new ArrayList<>();
 when(scheduleTaskRepository.findAllByJobIdAndJobType(DATA_ARCHIVE_TASK_ID, JOB_TYPE)).thenReturn(taskUnitsFromDb);

 assertEquals(NUM_OF_TASK_UNIT, taskUnitsFromDb.size());
 }
}