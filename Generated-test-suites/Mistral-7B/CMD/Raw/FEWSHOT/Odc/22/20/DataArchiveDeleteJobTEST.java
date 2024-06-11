package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getId()).thenReturn(1L);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(jsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class)).thenReturn(dataArchiveClearParameters);
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1);
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());
        doReturn(new ArrayList<>()).when(dataArchiveDeleteJob).executeTask(1L, new ArrayList<>());
        doReturn(TaskStatus.FAILED).when(dataArchiveDeleteJob).getTaskStatus(new ArrayList<>());
        doReturn(new ArrayList<>()).when(jsonUtils).fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
    }

    @After
    public void tearDown() {
        dataArchiveDeleteJob = null;
        taskEntity = null;
        context = null;
        jsonUtils = null;
        objectMapper = null;
        scheduleTaskRepository = null;
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        try {
            dataArchiveDeleteJob.execute(context);
            assertFalse("Expected method to throw JobExecutionException", false);
        } catch (JobExecutionException e) {
            assertEquals("Data archive task not found", e.getMessage());
        }

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById(taskEntity.getId()).get().getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() {
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        try {
            dataArchiveDeleteJob.execute(context);
            assertFalse("Expected method to throw JobExecutionException", false);
        } catch (JobExecutionException e) {
            assertEquals("Data archive task do not finish", e.getMessage());
        }

        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById(taskEntity.getId()).get().getStatus());
    }

    @Test
    public void testExecute_Success() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);

        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(mock(DlmTask.class));

        doReturn(dlmTasks).when(dataArchiveDeleteJob).executeTask(1L, new ArrayList<>());
        doReturn(TaskStatus.DONE).when(dataArchiveDeleteJob).getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.DONE, scheduleTaskRepository.findById(taskEntity.getId()).get().getStatus());
    }
}