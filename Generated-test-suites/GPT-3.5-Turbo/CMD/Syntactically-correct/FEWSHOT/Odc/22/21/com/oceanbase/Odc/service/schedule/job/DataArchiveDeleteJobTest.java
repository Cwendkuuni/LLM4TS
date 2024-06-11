import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

	private DataArchiveDeleteJob dataArchiveDeleteJob;
	private ScheduleTaskEntity taskEntity;
	private JobExecutionContext jobExecutionContext;

	@Before
	public void setup() {
		dataArchiveDeleteJob = new DataArchiveDeleteJob();
		taskEntity = new ScheduleTaskEntity();
		jobExecutionContext = Mockito.mock(JobExecutionContext.class);
		Mockito.when(jobExecutionContext.getResult()).thenReturn(taskEntity);
	}

	@Test
	public void testExecuteDataArchiveTaskNotFound() {
		DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
		dataArchiveClearParameters.setDataArchiveTaskId(1L);
		taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

		dataArchiveDeleteJob.execute(jobExecutionContext);
		
		assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
	}

	@Test
	public void testExecuteDataArchiveTaskNotFinished() {
		DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
		dataArchiveClearParameters.setDataArchiveTaskId(1L);
		taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

		ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
		dataArchiveTask.setStatus(TaskStatus.RUNNING);
		Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
			   .thenReturn(Optional.of(dataArchiveTask));

		dataArchiveDeleteJob.execute(jobExecutionContext);

		assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
	}

	@Test
	public void testExecuteTaskUnitsPreparation() {
		DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
		dataArchiveClearParameters.setDataArchiveTaskId(1L);
		taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

		ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
		dataArchiveTask.setId(1L);
		dataArchiveTask.setStatus(TaskStatus.DONE);
		dataArchiveTask.setResultJson(JsonUtils.toJson(new TypeReference<List<DlmTask>>() {}));
		Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
			   .thenReturn(Optional.of(dataArchiveTask));

		dataArchiveDeleteJob.execute(jobExecutionContext);

		assertNotNull(dataArchiveTask.getResultJson());
	}

	@Test
	public void testExecuteTaskUnitsExecution() {
		DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
		dataArchiveClearParameters.setDataArchiveTaskId(1L);
		taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

		ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
		dataArchiveTask.setId(1L);
		dataArchiveTask.setStatus(TaskStatus.DONE);
		dataArchiveTask.setResultJson(JsonUtils.toJson(new TypeReference<List<DlmTask>>() {}));
		Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
			   .thenReturn(Optional.of(dataArchiveTask));

		dataArchiveDeleteJob.execute(jobExecutionContext);

		assertEquals(TaskStatus.PREPARING, dataArchiveTask.getStatus());
	}

	@Test
	public void testGetTaskStatus() {
		List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
		TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

		assertNull(taskStatus);
	}
}