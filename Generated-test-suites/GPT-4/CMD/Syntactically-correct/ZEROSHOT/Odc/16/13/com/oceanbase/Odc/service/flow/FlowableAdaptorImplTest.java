package com.oceanbase.odc.service.flow;
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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;

import com.oceanbase.odc.metadb.flow.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl adaptor;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    private Long flowInstanceId = 123L;
    private String processInstanceId = "4321";

    @Before
    public void setUp() {
        // Setting up sample flow entities for testing
        FlowInstanceEntity sampleFlow = new FlowInstanceEntity();
        sampleFlow.setId(flowInstanceId);
        sampleFlow.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(sampleFlow));
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test logic goes here
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    // Similar tests to be written for each method of the class
}