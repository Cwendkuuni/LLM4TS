package org.mockito.internal.util.reflection;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    private GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        // Test case for non-generic field
        Field nonGenericField = TestClass.class.getDeclaredField("nonGenericField");
        Class nonGenericFieldType = genericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, nonGenericFieldType);

        // Test case for generic field
        Field genericField = TestClass.class.getDeclaredField("genericField");
        Class genericFieldType = genericMaster.getGenericType(genericField);
        assertEquals(String.class, genericFieldType);

        // Test case for nested generic field
        Field nestedGenericField = TestClass.class.getDeclaredField("nestedGenericField");
        Class nestedGenericFieldType = genericMaster.getGenericType(nestedGenericField);
        assertEquals(Object.class, nestedGenericFieldType);
    }

    // Add additional test cases for edge cases and exception handling scenarios here

    private static class TestClass {
        private String nonGenericField;
        private Class<? extends ParameterizedType> genericField;
        private Class<? extends Class> nestedGenericField;
    }
}



// Generate additional test cases for edge cases and exception handling scenarios here

