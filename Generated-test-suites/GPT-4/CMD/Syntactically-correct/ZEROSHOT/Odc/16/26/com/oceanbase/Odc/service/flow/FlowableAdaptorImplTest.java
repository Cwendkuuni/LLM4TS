
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
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Optional;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.instance.*;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import java.util.Collections;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.isA;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

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

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    //Rest of the initialization and test cases go here...
}
