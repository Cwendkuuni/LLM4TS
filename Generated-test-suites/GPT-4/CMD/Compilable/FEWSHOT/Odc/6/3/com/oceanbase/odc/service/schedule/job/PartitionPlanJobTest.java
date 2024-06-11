/*
* Copyright (c) 2023 OceanBase.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.*;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    ScheduleService scheduleService;

    @InjectMocks
    PartitionPlanJob partitionPlanJob;

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() throws Exception {
        ScheduleEntity mockScheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(mockScheduleEntity);
        when(mockScheduleEntity.getJobParametersJson()).thenReturn("{}");
        DatabasePartitionPlanEntity mockDatabasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(mockDatabasePartitionPlanEntity);
        when(mockDatabasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
        partitionPlanJob.execute(context);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
    }
}