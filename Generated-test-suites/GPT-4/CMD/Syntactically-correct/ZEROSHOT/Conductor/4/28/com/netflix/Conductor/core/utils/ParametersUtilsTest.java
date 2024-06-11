
/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
    }
    
    @Test
    public void testReplace_NullInput_ReturnNull() {
        assertNull(parametersUtils.replace(null, "sample_input"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplace_EmptyInput_ThrowsIllegalArgumentException() {
        parametersUtils.replace(new HashMap<>(), "");
    }

    @Test
    public void testReplace_VariableNotMatchesPattern_ReturnSameInput() {
        String json = "sample_input";
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("testKey", "testValue");

        Map<String, Object> response = parametersUtils.replace(input, json);
        assertEquals(input, response);
    }

    @Test
    public void testGetTaskInputV1_NullInput_ReturnsEmptyMap() {
        WorkflowModel mockWorkflowModel = mock(WorkflowModel.class);
        
        Map<String, Object> result = parametersUtils.getTaskInputV1(mockWorkflowModel, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Add more test cases to test the remaining methods in the ParametersUtils class
}
