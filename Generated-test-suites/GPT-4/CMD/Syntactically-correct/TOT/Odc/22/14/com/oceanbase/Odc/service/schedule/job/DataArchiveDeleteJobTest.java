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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.schedule.repo.ScheduleTaskRepository;

public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_noDataArchiveTask() {
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    // Other possible tests, such as when the task options is present or when status is not DONE,  goes here...
}