
/*
 * Copyright 2022 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private TaskDef taskDef;

    // Method Under Test : getTaskInput
    @Test
    public void testGetTaskInput_Default() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> result = parametersUtils.getTaskInput(new HashMap<>(), null, taskDef, "1234");
        assertNotNull(result);
        assertEquals("Expect to have an empty Map if no map found.", 0, result.size());
    }

    // Edge Case for method getTaskInputV2 : ObjectMapper throwing an IOException
    @Test(expected = RuntimeException.class)
    public void testGetTaskInputV2_ObjectMapper_IOException() throws Exception {
        Mockito.when(objectMapper.writeValueAsBytes(Mockito.anyObject())).thenThrow(new IOException());

        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        parametersUtils.getTaskInputV2(new HashMap<>(), null, "1234", taskDef);

        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsBytes(Mockito.anyObject());
    }

    // Method Under Test: getWorkflowInput
    @Test
    public void testGetWorkflowInput_Default() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> result = parametersUtils.getWorkflowInput(null, new HashMap<>());
        assertNotNull(result);
        assertEquals("Expect to have an empty Map if no map found.", 0, result.size());
    }
}
