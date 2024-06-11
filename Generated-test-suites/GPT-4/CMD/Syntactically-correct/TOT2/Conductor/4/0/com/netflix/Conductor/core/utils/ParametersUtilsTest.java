package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import com.netflix.conductor.core.utils.ParametersUtils;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput() {
        // todo
    }

    @Test
    public void testGetTaskInputV2() {
        // todo
    }

    @Test
    public void testReplace_Map() {
        // todo
    }

    @Test
    public void testReplace_String() {
        // todo
    }

    @Test
    public void testGetWorkflowInput() {
        // todo
    }

}
