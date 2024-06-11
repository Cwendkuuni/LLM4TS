package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private ScheduleEntity scheduleEntity;

    @Before
    public void setup() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setSpringContextUtil(springContextUtil);
    }

    @Test
    public void testExecute_validScheduleEntity_partitionsPlansExecuted() {
        ScheduleEntity validScheduleEntity = new ScheduleEntity();
        validScheduleEntity.setId(UUID.randomUUID().toString());
        validScheduleEntity.setJobParametersJson(JsonUtils.toJson(getValidJobParameters()));
        PartitionPlanJobParameters jobParameters = getValidJobParameters();

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(jobParameters.getDatabasePartitionPlanId());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(getValidTablePartitionPlan1(), getValidTablePartitionPlan2());

        when(scheduleService.nullSafeGetById(validScheduleEntity.getId())).thenReturn(validScheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(new JobExecutionContext() {
            @Override
            public String getJobDetailName() {
                return "TestJob";
            }

            @Override
            public String getJobDetailGroupName() {
                return "TestGroup";
            }

            @Override
            public JobDataMap getJobDataMap() {
                return new JobDataMap();
            }

            @Override
            public Trigger trigger() {
                return null;
            }

            @Override
            public JobExecutionContext getPreviousFireTime() {
                return null;
            }
        });

        // Assertions
        assertNull(validScheduleEntity.getEndTime());
    }

    @Test
    public void testExecute_invalidScheduleEntity_jobNotExecuted() {
        ScheduleEntity invalidScheduleEntity = new ScheduleEntity();
        invalidScheduleEntity.setId(UUID.randomUUID().toString());

        when(scheduleService.nullSafeGetById(invalidScheduleEntity.getId())).thenThrow(new SchedulerException());

        partitionPlanJob.execute(new JobExecutionContext() {
            @Override
            public String getJobDetailName() {
                return "TestJob";
            }

            @Override
            public String getJobDetailGroupName() {
                return "TestGroup";
            }

            @Override
            public JobDataMap getJobDataMap() {
                return new JobDataMap();
            }

            @Override
            public Trigger trigger() {
                return null;
            }

            @Override
            public JobExecutionContext getPreviousFireTime() {
                return null;
            }
        });

        // Assertions
        assertNull(invalidScheduleEntity.getEndTime());
    }

    @Test
    public void testExecute_disabledDatabasePartitionPlan_jobNotExecuted() {
        ScheduleEntity validScheduleEntity = new ScheduleEntity();
        validScheduleEntity.setId(UUID.randomUUID().toString());
        validScheduleEntity.setJobParametersJson(JsonUtils.toJson(getValidJobParameters()));
        PartitionPlanJobParameters jobParameters = getValidJobParameters();

        DatabasePartitionPlanEntity disabledDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        disabledDatabasePartitionPlan.setId(jobParameters.getDatabasePartitionPlanId());
        disabledDatabasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(getValidTablePartitionPlan1(), getValidTablePartitionPlan2());

        when(scheduleService.nullSafeGetById(validScheduleEntity.getId())).thenReturn(validScheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(disabledDatabasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        assertThrows(RuntimeException.class, () -> {
            partitionPlanJob.execute(new JobExecutionContext() {
                @Override
                public String getJobDetailName() {
                    return "TestJob";
                }

                @Override
                public String getJobDetailGroupName() {
                    return "TestGroup";
                }

                @Override
                public JobDataMap getJobDataMap() {
                    return new JobDataMap();
                }

                @Override
                public Trigger trigger() {
                    return null;
                }

                @Override
                public JobExecutionContext getPreviousFireTime() {
                    return null;
                }
            });
        });

        // Assertions
        assertNull(validScheduleEntity.getEndTime());
    }

    @Test
    public void testExecute_emptyTablePartitionPlans_jobNotExecuted() {
        ScheduleEntity validScheduleEntity = new ScheduleEntity();
        validScheduleEntity.setId(UUID.randomUUID().toString());
        validScheduleEntity.setJobParametersJson(JsonUtils.toJson(getValidJobParameters()));
        PartitionPlanJobParameters jobParameters = getValidJobParameters();

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(jobParameters.getDatabasePartitionPlanId());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> emptyTablePartitionPlans = List.of();

        when(scheduleService.nullSafeGetById(validScheduleEntity.getId())).thenReturn(validScheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(emptyTablePartitionPlans);

        assertThrows(RuntimeException.class, () -> {
            partitionPlanJob.execute(new JobExecutionContext() {
                @Override
                public String getJobDetailName() {
                    return "TestJob";
                }

                @Override
                public String getJobDetailGroupName() {
                    return "TestGroup";
                }

                @Override
                public JobDataMap getJobDataMap() {
                    return new JobDataMap();
                }

                @Override
                public Trigger trigger() {
                    return null;
                }

                @Override
                public JobExecutionContext getPreviousFireTime() {
                    return null;
                }
            });
        });

        // Assertions
        assertNull(validScheduleEntity.getEndTime());
    }

    @Test
    public void testInterrupt_unexpectedExceptionThrown() {
        assertThrows(UnExpectedException.class, () -> {
            partitionPlanJob.interrupt();
        });
    }

    private PartitionPlanJobParameters getValidJobParameters() {
        return new PartitionPlanJobParameters();
    }

    private TablePartitionPlanEntity getValidTablePartitionPlan1() {
        return new TablePartitionPlanEntity();
    }

    private TablePartitionPlanEntity getValidTablePartitionPlan2() {
        return new TablePartitionPlanEntity();
    }
}