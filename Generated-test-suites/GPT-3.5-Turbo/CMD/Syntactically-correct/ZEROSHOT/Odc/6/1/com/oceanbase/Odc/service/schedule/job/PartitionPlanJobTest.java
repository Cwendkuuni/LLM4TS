import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext mockJobContext;

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private ScheduleService mockScheduleService;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        SpringContextUtil springContextUtilMock = mock(SpringContextUtil.class);

        when(springContextUtilMock.getBean(PartitionPlanTaskService.class)).thenReturn(mockPartitionPlanTaskService);
        when(springContextUtilMock.getBean(PartitionPlanService.class)).thenReturn(mockPartitionPlanService);
        when(springContextUtilMock.getBean(ScheduleService.class)).thenReturn(mockScheduleService);

        partitionPlanJob.springContextUtil = springContextUtilMock;
    }

    @Test
    public void testExecute() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");

        when(ScheduleTaskUtils.getScheduleId(mockJobContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(mockJobContext);

        verify(mockScheduleService).nullSafeGetById(scheduleId);
        verify(mockPartitionPlanService).getDatabasePartitionPlanById(anyLong());
        verify(mockPartitionPlanService).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(mockScheduleService).terminate(scheduleEntity);
        verify(mockPartitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}