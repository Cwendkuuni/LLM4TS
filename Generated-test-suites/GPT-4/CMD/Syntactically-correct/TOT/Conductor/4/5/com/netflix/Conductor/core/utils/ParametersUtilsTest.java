package com.netflix.conductor.core.utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTests {

    @Mock
    ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void getWorkflowInputTest() throws Exception {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "inputValue");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(inputParams);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());
        assertEquals(result.get("inputKey"), "inputValue");
    }

    @Test(expected = RuntimeException.class)
    public void cloneTestException() throws Exception{
        parametersUtils.clone(new HashMap<>());
    }

    @Test
    public void cloneTest() throws Exception{
        when(objectMapper.writeValueAsBytes(anyMap())).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyMap(), anyMap())).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtils.clone(new HashMap<>());
        assertNotNull(result);
    }

    @Test
    public void replaceTest() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");
        Map<String, Object> result = parametersUtils.replace(input, new HashMap<>());
        assertTrue(result.containsKey("key"));
        assertTrue(result.containsValue("value"));
    }
  
    // similar methods for testing other fragments of the code would go here
  
    @Test
    public void getTaskInputTest() {
       // implementation of the test case here
    }

    @Test
    public void getTaskInputV2Test() {
       // implementation of the test case here
    }

    @Test
    public void replaceListTest() {
       // implementation of the test case here
    }

    @Test
    public void replaceVariablesTest() {
       // implementation of the test case here
    }

    @Test
    public void getTaskInputV1Test() {
       // implementation of the test case here
    }
}