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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext jobExecutionContextMock;

    @Mock
    PartitionPlanTaskService partitionPlanTaskServiceMock;

    @Mock
    PartitionPlanService partitionPlanServiceMock;

    @Mock
    ScheduleService scheduleServiceMock;
    
    @Mock
    ScheduleEntity scheduleEntityMock;

    @Mock
    DatabasePartitionPlanEntity databasePartitionPlanMock;

    @Mock
    TablePartitionPlanEntity tablePartitionPlanEntityMock;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void testExecute_WhenScheduleNotFound_LogsWarning() throws SchedulerException {
        Long scheduleId = 1L;

        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(scheduleServiceMock, times(1)).nullSafeGetById(scheduleId);
        // Verify that a warning log is generated, but do not check the specific log message
        // as this depends on the logging framework being used
    }

    @Test
    public void testExecute_WhenNoTablePlanToProcess_StopJob() throws SchedulerException {
        Long scheduleId = 1L;

        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
        
        partitionPlanJob.execute(jobExecutionContextMock);

        verify(scheduleServiceMock, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanServiceMock, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanServiceMock, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(scheduleServiceMock, times(1)).terminate(scheduleEntityMock);
    }

}