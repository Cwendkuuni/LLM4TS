package com.oceanbase.odc.service.schedule.job;

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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService mockPartitionPlanTaskService;
    private PartitionPlanService mockPartitionPlanService;
    private ScheduleService mockScheduleService;
    private JobExecutionContext mockJobExecutionContext;

    @Before
    public void setUp() {
        mockPartitionPlanTaskService = mock(PartitionPlanTaskService.class);
        mockPartitionPlanService = mock(PartitionPlanService.class);
        mockScheduleService = mock(ScheduleService.class);
        mockJobExecutionContext = mock(JobExecutionContext.class);

        SpringContextUtil contextMock = mock(SpringContextUtil.class);
        when(contextMock.getBean(PartitionPlanTaskService.class)).thenReturn(mockPartitionPlanTaskService);
        when(contextMock.getBean(PartitionPlanService.class)).thenReturn(mockPartitionPlanService);
        when(contextMock.getBean(ScheduleService.class)).thenReturn(mockScheduleService);
        partitionPlanJob = new PartitionPlanJob(contextMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() throws SchedulerException {
        when(mockJobExecutionContext.getMergedJobDataMap()).thenReturn(null);
        partitionPlanJob.execute(mockJobExecutionContext);

        ScheduleEntity mockScheduleEntity = createMockScheduleEntity();
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(1L);
        when(mockScheduleService.nullSafeGetById(1L)).thenReturn(mockScheduleEntity);

        DatabasePartitionPlanEntity mockDbPartitionEntity = createMockDbPartitionPlanEntity();
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(mockDbPartitionEntity);

        List<TablePartitionPlanEntity> mockTablePartitionPlans = createMockTablePartitionPlanEntityList();
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(mockTablePartitionPlans);
        partitionPlanJob.execute(mockJobExecutionContext);
    }

    private ScheduleEntity createMockScheduleEntity() {
        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobParametersJson(JsonUtils.toJson(new PartitionPlanJobParameters()));
        return mockScheduleEntity;
    }

    private DatabasePartitionPlanEntity createMockDbPartitionPlanEntity() {
        DatabasePartitionPlanEntity mockDbPartitionEntity = new DatabasePartitionPlanEntity();
        mockDbPartitionEntity.setConfigEnabled(true);
        return mockDbPartitionEntity;
    }

    private List<TablePartitionPlanEntity> createMockTablePartitionPlanEntityList() {
        TablePartitionPlanEntity mockTppe = new TablePartitionPlanEntity();
        return Arrays.asList(mockTppe);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnScheduleServiceTerminate() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(1L);
        when(mockScheduleService.nullSafeGetById(1L)).thenThrow(new RuntimeException());
        partitionPlanJob.execute(mockJobExecutionContext);
    }
}
