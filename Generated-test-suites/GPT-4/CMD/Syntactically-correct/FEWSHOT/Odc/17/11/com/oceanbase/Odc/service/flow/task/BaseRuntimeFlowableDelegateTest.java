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
package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {
    
    @InjectMocks
    private MockRuntimeFlowableDelegate mockRuntimeFlowableDelegate;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private DelegateExecution execution;

    @Before
    public void setUp() {
        mockRuntimeFlowableDelegate.init(eventPublisher);
    }

    @Test
    public void testExecuteDelegate() {
        mockRuntimeFlowableDelegate.execute(execution);
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        assertFalse(mockRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        mockRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);
        // No specific assertion because the method doesn't return any value, 
	// so we just confirm that there is no exception when calling it.
    }

    private static final class MockRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Void> {

        @Override
        protected Callable<Void> initCallable(DelegateExecution execution) {
            return () -> null;
        }
    }
}