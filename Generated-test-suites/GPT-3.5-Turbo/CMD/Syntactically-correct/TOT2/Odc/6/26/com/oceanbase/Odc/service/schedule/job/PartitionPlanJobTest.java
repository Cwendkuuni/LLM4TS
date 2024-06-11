import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private JobExecutionContext jobExecutionContextMock;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        jobExecutionContextMock = mock(JobExecutionContext.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void testExecute() throws SchedulerException {
        // Typical use case
        when(ScheduleTaskUtils.getScheduleId(any())).thenReturn(1L);

        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParametersMock = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlansMock = mock(List.class);

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(anyString(), eq(PartitionPlanJobParameters.class))).thenReturn(jobParametersMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlansMock);

        partitionPlanJob.execute(jobExecutionContextMock);

        // Edge case
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(false);
        when(tablePartitionPlansMock.isEmpty()).thenReturn(true);

        partitionPlanJob.execute(jobExecutionContextMock);

        // Error scenario
        doThrow(new SchedulerException()).when(scheduleServiceMock).terminate(any());
        doThrow(new Exception()).when(partitionPlanTaskServiceMock)
                .executePartitionPlan(anyLong(), any(List.class));

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}