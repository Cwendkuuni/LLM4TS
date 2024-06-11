package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private ScheduleService mockScheduleService;

    @Mock
    private JobExecutionContext mockJobExecutionContext;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws Exception {
        when(mockScheduleService.nullSafeGetById(any(Long.class))).thenReturn(new ScheduleEntity());
        when(mockPartitionPlanService.getDatabasePartitionPlanById(any(Long.class))).thenReturn(new DatabasePartitionPlanEntity());
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(new ArrayList<>());

        partitionPlanJob.execute(mockJobExecutionContext);

        verify(mockPartitionPlanTaskService, never()).executePartitionPlan(any(Long.class), anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_ExceptionThrown() throws Exception {
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(true);
        when(mockScheduleService.nullSafeGetById(any(Long.class))).thenReturn(new ScheduleEntity());
        when(mockPartitionPlanService.getDatabasePartitionPlanById(any(Long.class))).thenReturn(databasePartitionPlanEntity);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any(Long.class))).thenReturn(new ArrayList<>());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
