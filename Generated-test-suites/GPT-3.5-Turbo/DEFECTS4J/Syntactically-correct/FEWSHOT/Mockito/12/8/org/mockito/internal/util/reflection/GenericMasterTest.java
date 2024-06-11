package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test for non-generic field
        Field nonGenericField = null; // set non-generic field instance here
        assertEquals(Object.class, genericMaster.getGenericType(nonGenericField));

        // Test for generic field
        Field genericField = null; // set generic field instance here
        ParameterizedType genericType = (ParameterizedType) genericField.getGenericType();
        Type actualType = genericType.getActualTypeArguments()[0];
        Class expectedType = (Class) actualType;
        assertEquals(expectedType, genericMaster.getGenericType(genericField));
    }
}