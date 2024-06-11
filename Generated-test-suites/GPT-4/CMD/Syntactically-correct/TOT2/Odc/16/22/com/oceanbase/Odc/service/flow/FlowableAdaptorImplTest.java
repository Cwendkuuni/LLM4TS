package com.oceanbase.odc.service.flow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testId";
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testId";
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testDefinitionId";
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "testDefinitionId";
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

// Add other test cases in the same way for each methods

}