
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    
    @Mock
    private PartitionPlanService partitionPlanService;
    
    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private JobExecutionContext context;
    
    @InjectMocks
    private PartitionPlanJob partitionPlanJob;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected=UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap()).thenReturn(new JobDataMap(Collections.singletonMap(ScheduleTaskUtils.SCHEDULE_ID, scheduleId)));
        
        DatabasePartitionPlanEntity databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1)).thenReturn(databasePartitionPlanEntity);
        
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1)).thenReturn(Collections.singletonList(tablePartitionPlanEntity));
        
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }
}
