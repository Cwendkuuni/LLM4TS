
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Test with a generic field
        Field genericField = Util.class.getDeclaredField("genericField");
        Class<?> result = GenericMaster.getGenericType(genericField);
        assertEquals(String.class, result);

        // Test with a non-generic field
        Field nonGenericField = Util.class.getDeclaredField("nonGenericField");
        result = GenericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, result);

        // Test with a null field
        result = GenericMaster.getGenericType(null);
        assertEquals(Object.class, result);
    }
}
