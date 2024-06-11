package com.netflix.conductor.core.utils;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    //initialize Dependencies like ObjectMapper which is used in class constructor
    @Before
    public void init() {
        parametersUtils = new ParametersUtils(objectMapper); 
    }

    @Test
    public void getWorkflowInputTest_EmptyInputTemplate() {
    	//Arrange
	WorkflowDef workflowDef = mock(WorkflowDef.class);
	Map<String, Object> inputParams = new HashMap<>();
	inputParams.put("key", "value");

	//Act
	Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

	//Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("value", result.get("key"));
	Mockito.verify(workflowDef, times(1)).getInputTemplate();

    }

    // Test for if Null workflow Definition is passed should return same inputParams back 
    @Test
    public void getWorkflowInputTest_NullWorkflowDefinition() {
	//Arrange
	Map<String, Object> inputParams = new HashMap<>();
	inputParams.put("key", "value");

	//Act
	Map<String, Object> result = parametersUtils.getWorkflowInput(null, inputParams);

	//Assert
	Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("value", result.get("key"));

    }

    @Test
    public void replaceTest_MapInput_SuccessFullReplacement() throws Exception {
	//Arrange
	Map<String, Object> input = new HashMap<>();
        input.put("foo", "bar");
        DocumentContext doc = JsonPath.parse("{}");

        //Act
        Map<String, Object> result = parametersUtils.replace(input, doc);

        //Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(input, result);
        Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsBytes(input);
    }
}
