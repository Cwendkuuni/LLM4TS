/**
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

import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob subject;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    //Add here your mock instances for the other dependencies.

    @Before
    public void setup() {
        //Here you set your baseline expected behaviors.
    }

    @Test(expected = Exception.class)
    public void testExecute_Exception() {
        // Configure a scenario where scheduleEntity is null
        // And test if the expected exception occurs
        // Given 
        //When 
        //Then
    }

    @Test
    public void testExecute_Success() {
        // Configure a successful scenario 
        // And test if the expected behavior occurs
        // Given 
        //When 
        //Then
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt_UnExpectedException() {
        subject.interrupt();
    }
}