package org.mockito.internal.util.reflection;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = null;
        Class result = null;

        // Test1: when field is null
        result = GenericMaster.getGenericType(field);
        assertEquals(Object.class, result);
        
        // Test2: when the field is not generic
        field = new Field(); // replace with a non-generic field
        result = GenericMaster.getGenericType(field);
        assertEquals(Object.class, result);
        
        // Test3: when the field is a generic type
        field = new Field(); // replace with a generic field
        result = GenericMaster.getGenericType(field);
        Type genericType = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        assertEquals(actualTypeArgument, result);
    }
}