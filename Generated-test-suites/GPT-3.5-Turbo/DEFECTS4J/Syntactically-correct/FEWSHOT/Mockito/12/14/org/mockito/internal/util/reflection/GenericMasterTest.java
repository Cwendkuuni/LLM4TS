
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class GenericMasterTest {
    GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType() throws NoSuchFieldException, SecurityException {
        // Test case 1: field is generic with parameterized type
        Field genericField = MyClass.class.getDeclaredField("genericField");
        Class expected1 = String.class;
        Class result1 = genericMaster.getGenericType(genericField);
        assertNotNull(result1);
        assertEquals(expected1, result1);

        // Test case 2: field is generic with nested parameterized type
        Field nestedGenericField = MyClass.class.getDeclaredField("nestedGenericField");
        Class expected2 = Integer.class;
        Class result2 = genericMaster.getGenericType(nestedGenericField);
        assertNotNull(result2);
        assertEquals(expected2, result2);

        // Test case 3: field is not generic
        Field nonGenericField = MyClass.class.getDeclaredField("nonGenericField");
        Class expected3 = Object.class;
        Class result3 = genericMaster.getGenericType(nonGenericField);
        assertNotNull(result3);
        assertEquals(expected3, result3);
    }

    private class MyClass {
        private List<String> genericField;
        private Map<String, Integer> nestedGenericField;
        private String nonGenericField;
    }
}
