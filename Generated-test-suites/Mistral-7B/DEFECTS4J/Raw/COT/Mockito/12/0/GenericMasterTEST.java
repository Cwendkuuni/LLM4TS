package org.junit.tests.genericmaster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = SomeClassWithGenericField.class;
        Field field = clazz.getDeclaredField("genericField");
        field.setAccessible(true);

        // Test normal case
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);

        // Test when field is not generic
        Field notGenericField = clazz.getDeclaredField("nonGenericField");
        notGenericField.setAccessible(true);
        assertSame(Object.class, genericMaster.getGenericType(notGenericField));

        // Test when generic type is not an instance of ParameterizedType
        Field fieldWithoutTypeArguments = clazz.getDeclaredField("fieldWithoutTypeArguments");
        fieldWithoutTypeArguments.setAccessible(true);
        assertSame(Object.class, genericMaster.getGenericType(fieldWithoutTypeArguments));

        // Test null field
        assertNull(genericMaster.getGenericType(null));
    }

    // Add more test cases here for other potential edge cases or exception handling scenarios

    private static class SomeClassWithGenericField {
        @SuppressWarnings("unused")
        private List<String> genericField;
    }
}