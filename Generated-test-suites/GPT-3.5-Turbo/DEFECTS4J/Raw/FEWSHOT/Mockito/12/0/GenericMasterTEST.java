package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_withGenericField_shouldReturnGenericType() throws NoSuchFieldException {
        Field genericField = MyClass.class.getDeclaredField("genericField");
        Class expectedType = String.class;

        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(genericField);

        assertSame(expectedType, actualType);
    }
    
    @Test
    public void testGetGenericType_withNonGenericField_shouldReturnObjectClass() throws NoSuchFieldException {
        Field nonGenericField = MyClass.class.getDeclaredField("nonGenericField");
        Class expectedType = Object.class;

        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(nonGenericField);

        assertSame(expectedType, actualType);
    }

    private static class MyClass<T> {
        private List<String> genericField;
        private String nonGenericField;
    }
}