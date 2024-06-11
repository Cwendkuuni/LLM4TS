
/*
 * Copyright 2022 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync("test_workflow_01");
        verify(workflowSweeper, times(1)).sweep("test_workflow_01");
    }

    @Test(expected = NotFoundException.class)
    public void testSweepNotFound() {
        doThrow(NotFoundException.class).when(workflowRepairService).verifyAndRepairWorkflowTasks("test_workflow_01");
        workflowSweeper.sweep("test_workflow_01");
    }

    @Test(expected = IllegalStateException.class)
    public void testSweepException() {
        doThrow(IllegalStateException.class).when(workflowRepairService).verifyAndRepairWorkflowTasks("test_workflow_01");
        workflowSweeper.sweep("test_workflow_01");
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long offset = workflowSweeper.workflowOffsetWithJitter(45);
        assert(offset <=60 && offset >=30);
    }

}
