package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.Long;
import java.lang.String;
import java.util.Optional;

public class FlowableAdaptorImplTest {

  @InjectMocks
  private FlowableAdaptorImpl flowableAdaptorImpl;

  @Mock
  private NodeInstanceEntityRepository nodeInstanceRepository;

  @Mock
  private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

  @Mock
  private EventPublisher eventPublisher;

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

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSetProcessInstanceId() {
    Long flowInstanceId = Long.valueOf(99999);
    String processInstanceId = "process100";
    flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
    verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
  }

  @Test
  public void testGetFlowInstanceIdByProcessInstanceId() {
    String processInstanceId = "process100";
    flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
    verify(flowInstanceRepository, times(1)).findAll(any());
  }

  // Need to continue for creating test cases for the rest of the public methods.
  // Similarly, the edge cases and exceptions also need to be identified and corresponding test cases need to be created.
}
