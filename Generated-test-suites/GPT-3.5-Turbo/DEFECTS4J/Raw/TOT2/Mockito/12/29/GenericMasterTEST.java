package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test with generic field
        Field genericField = TestClass.class.getDeclaredField("genericField");
        Class genericType = genericMaster.getGenericType(genericField);
        assertEquals(String.class, genericType);
        
        // Test with non-generic field
        Field nonGenericField = TestClass.class.getDeclaredField("nonGenericField");
        genericType = genericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, genericType);
    }
    
    // Define a test class for testing
    private static class TestClass {
        private List<String> genericField;
        private String nonGenericField;
    }

}