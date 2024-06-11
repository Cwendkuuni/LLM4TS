package com.oceanbase.odc.service.flow.instance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity entity;

    @Mock
    private RuntimeDelegateMapper convertor;

    @Mock
    FlowableAdaptor flowableAdaptor;

    @Mock
    EventPublisher eventPublisher;

    @Mock
    TaskService taskService;

    @Mock
    NodeInstanceEntityRepository nodeRepository;

    @Mock
    SequenceInstanceRepository sequenceRepository;

    @Mock
    ServiceTaskInstanceRepository serviceTaskRepository;

    FlowTaskInstance instance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(entity.getId()).thenReturn(1L);

        instance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, instance.getCoreFlowableElementType());
    }

    @Test
    public void testIsPresentOnThisMachineNotPresent() {
        try {
            instance.isDone();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            //
        }
    }

    @Test
    public void testIsDoneIsPresent() {
      //Mock and setup a present object
      //Mockito.when(instance.isPresentOnThisMachine()).thenReturn(true);

      //assertEquals(true, instance.isDone());
    }

    // Create tests similar to testIsDoneIsPresent for other methods
}