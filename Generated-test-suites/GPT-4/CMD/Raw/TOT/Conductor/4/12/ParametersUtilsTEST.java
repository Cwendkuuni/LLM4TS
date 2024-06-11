package com.netflix.conductor.core.utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {
    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    private Map<String, Object> inputParams;

    @Before
    public void setUp() throws Exception {
        parametersUtils = new ParametersUtils(objectMapper);

        inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");
    }

    @Test
    public void testClone() throws Exception {
        Map<String, Object> clonedParams = new HashMap<>();
        clonedParams.put("key1", "value1");
        clonedParams.put("key2", "value2");
    
        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any(TypeReference.class))).thenReturn(clonedParams);
    
        Map<String, Object> result = parametersUtils.clone(inputParams);
    
        assertEquals(clonedParams, result);
    
        verify(objectMapper).writeValueAsBytes(inputParams);
        verify(objectMapper).readValue(new byte[0], new TypeReference<>() {});
    }

    @Test
    public void testReplace() {
        // Fill this with the test for the replace method
    }
  
    @Test
    public void testReplaceVariables() {
        // Fill this with the test for the replaceVariables method
    }
  
    @Test
    public void testReplaceList() {
        // Fill this with the test for the replaceList method
    }
  
    @Test
    public void testGetTaskInput() {
        // Fill this with the test for the getTaskInput method
    }
}