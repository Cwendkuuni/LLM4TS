
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
    }

    @Test(expected = UnExpectedException.class)
    public void interruptTest() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void executeTest() throws SchedulerException {
        Long scheduleId = 1L;
        TablePartitionPlanEntity tppEntity = mock(TablePartitionPlanEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity dppEntity = mock(DatabasePartitionPlanEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn("json");
        when(dppEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(dppEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Collections.singletonList(tppEntity));

        partitionPlanJob.execute(context);
        
        verify(scheduleService, never()).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(dppEntity.getFlowInstanceId(),Collections.singletonList(tppEntity));
    }

}
