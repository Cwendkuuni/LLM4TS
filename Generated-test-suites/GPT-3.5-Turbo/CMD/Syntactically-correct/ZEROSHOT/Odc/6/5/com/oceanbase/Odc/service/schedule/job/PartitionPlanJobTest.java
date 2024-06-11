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

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        SpringContextUtil springContextUtilMock = mock(SpringContextUtil.class);
        when(springContextUtilMock.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(springContextUtilMock.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext contextMock = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntityMock = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParametersMock = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanMock = mock(TablePartitionPlanEntity.class);

        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(contextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);
        when(JsonUtils.fromJson(scheduleEntityMock.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParametersMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParametersMock.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParametersMock.getDatabasePartitionPlanId())).thenReturn(List.of(tablePartitionPlanMock));
        
        partitionPlanJob.execute(contextMock);

        verify(scheduleEntityMock, times(1)).getJobParametersJson();
        verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(jobParametersMock.getDatabasePartitionPlanId());
        verify(partitionPlanServiceMock, times(1)).getValidTablePlanByDatabasePartitionPlanId(jobParametersMock.getDatabasePartitionPlanId());
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlanMock.getFlowInstanceId(), List.of(tablePartitionPlanMock));
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}