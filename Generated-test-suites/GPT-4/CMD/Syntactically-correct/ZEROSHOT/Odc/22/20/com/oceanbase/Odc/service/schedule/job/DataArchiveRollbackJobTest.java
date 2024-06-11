package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.junit.Before;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DlmTask taskUnit;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private com.oceanbase.odc.common.json.JsonUtils jsonUtils;

    @Mock
    private com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil dlmJobIdUtil;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("parametersJson");
        when(jsonUtils.fromJson("parametersJson", DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(scheduleTaskRepository.findById("dataArchiveTaskId"))
                .thenReturn(Optional.of(taskEntity));
        when(taskUnit.getSourceDatabaseId()).thenReturn(1L);
        when(dlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", "id", 1))
                .thenReturn(1L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById("id", TaskStatus.DONE);
    }
}
