package com.netflix.conductor.core.utils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {
   
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp(){
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace(){
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${jsonpath1}");
        input.put("key2", "${jsonpath2}");

        Map<String, Object> json = new HashMap<>();
        json.put("jsonpath1", "value1");
        json.put("jsonpath2", "value2");

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testReplaceString(){
        String paramString = "${jsonpath}";

        Object result = parametersUtils.replace(paramString);

        assertEquals("{}", result.toString());
    }

    @Test
    public void testGetWorkflowInput_noInputTemplate() {
        WorkflowDef def = new WorkflowDef();
        def.setInputTemplate(null);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "inputValue");

        Map<String, Object> result = parametersUtils.getWorkflowInput(def, inputParams);

        assertEquals(inputParams, result);
    }

    @Test
    public void testGetWorkflowInput_withInputTemplate() throws Exception {
        WorkflowDef def = new WorkflowDef();

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("templateKey", "templateValue");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "inputValue");

        Map<String, Object> clonedInputTemplate = new HashMap<>(inputTemplate);

        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], Map.class)).thenReturn(clonedInputTemplate);

        def.setInputTemplate(inputTemplate);

        Map<String, Object> result = parametersUtils.getWorkflowInput(def, inputParams);

        Map<String, Object> expectedResults = new HashMap<>();
        expectedResults.put("templateKey", "templateValue");
        expectedResults.put("inputKey", "inputValue");
        assertEquals(expectedResults, result);       
    }
}