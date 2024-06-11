package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanJobParameters partitionPlanJobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;
    
    @Mock
    private TablePartitionPlanEntity tablePartitionPlanEntity;
    
    @Before
    public void setUp(){
        //initialize all the mocks
    }

    @Test
    public void testExecute() throws SchedulerException {
        List<TablePartitionPlanEntity> tablePlanList = new ArrayList<>();
        tablePlanList.add(tablePartitionPlanEntity);
        when(scheduleService.nullSafeGetById(any())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("fake-json");
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any()))
                .thenReturn(tablePlanList);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            fail("Not expecting any exception. If thrown, the test fails.");
        }
        verify(databasePartitionPlanEntity, times(1)).isConfigEnabled();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}