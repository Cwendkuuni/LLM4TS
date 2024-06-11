package com.fasterxml.jackson.databind.deser.std;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {
    
    private StdKeyDeserializer keyDeserializer;
    
    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }
    
    @After
    public void tearDown() {
        keyDeserializer = null;
    }
    
    @Test
    public void testDeserializeKey_withValidString_shouldReturnString() throws Exception {
        // Arrange
        String key = "test";
        
        // Act
        Object result = keyDeserializer.deserializeKey(key, null);
        
        // Assert
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_withNullString_shouldReturnNull() throws Exception {
        // Arrange
        String key = null;
        
        // Act
        Object result = keyDeserializer.deserializeKey(key, null);
        
        // Assert
        assertNull(result);
    }
    
    @Test(expected = RuntimeException.class)
    public void testDeserializeKey_withInvalidString_shouldThrowException() throws Exception {
        // Arrange
        String key = "invalid";
        
        // Act
        keyDeserializer.deserializeKey(key, null);
        
        // Assert
        // Exception expected
    }
}