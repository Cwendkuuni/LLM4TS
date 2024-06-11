package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testStdKeyDeserializer() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        
        assertEquals(Integer.class, stdKeyDeserializer.getKeyClass());
        
        // Test typical use case
        String key = "123";
        try {
            int result = (int) stdKeyDeserializer.deserializeKey(key, null);
            assertEquals(123, result);
        } catch (Exception e) {
            fail("Exception thrown during typical use case test");
        }
        
        // Test edge case
        key = String.valueOf(Integer.MAX_VALUE + 1L);
        try {
            stdKeyDeserializer.deserializeKey(key, null);
            fail("Did not throw exception for integer overflow");
        } catch (Exception e) {
            // Expected exception for integer overflow
        }
        
        // Test error scenario
        key = "abc";
        try {
            stdKeyDeserializer.deserializeKey(key, null);
            fail("Did not throw exception for invalid key");
        } catch (Exception e) {
            // Expected exception for invalid key
        }
    }

    @Test
    public void testStringKD() {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        
        assertEquals(String.class, stringKD.getKeyClass());
        
        // Test typical use case
        String key = "test";
        try {
            String result = (String) stringKD.deserializeKey(key, null);
            assertEquals("test", result);
        } catch (Exception e) {
            fail("Exception thrown during typical use case test");
        }
        
        // Test edge case
        key = null;
        try {
            String result = (String) stringKD.deserializeKey(key, null);
            assertNull(result);
        } catch (Exception e) {
            fail("Exception thrown during edge case test");
        }
    }

    // Add more test cases for other methods if needed

}