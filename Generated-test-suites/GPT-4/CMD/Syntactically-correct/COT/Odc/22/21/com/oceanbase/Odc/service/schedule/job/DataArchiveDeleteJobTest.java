package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext mockContext;

    @Mock
    private ScheduleTaskEntity mockTaskEntity;

    @Mock
    private DataArchiveClearParameters mockDataArchiveClearParameters;

    @Mock
    private ScheduleTaskEntity mockDataArchiveTask;

    @Mock
    private List<DlmTask> mockTaskUnits;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {

        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        when(mockTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"12345\"}");
        when(mockDataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("12345");

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(mockContext);

        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(mockDataArchiveTask.getResultJson()).thenReturn("[{\"id\": \"12345\", \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]");

        dataArchiveDeleteJob.execute(mockContext);
    }

}