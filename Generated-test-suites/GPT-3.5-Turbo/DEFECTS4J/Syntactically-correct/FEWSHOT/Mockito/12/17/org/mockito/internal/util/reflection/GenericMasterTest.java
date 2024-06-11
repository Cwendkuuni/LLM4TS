package org.mockito.internal.util.reflection;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test case 1
        Field field1 = null;
        assertEquals(Object.class, genericMaster.getGenericType(field1));

        // Test case 2
        Field field2 = new Field();
        assertEquals(Object.class, genericMaster.getGenericType(field2));

        // Test case 3
        Field field3 = new Field();
        field3.setGenericType(ParameterizedType.class);
        Type[] actualTypeArguments = new Type[1];
        actualTypeArguments[0] = Integer.class;
        ParameterizedType parameterizedType = new ParameterizedType();
        parameterizedType.setActualTypeArguments(actualTypeArguments);
        field3.setGenericType(parameterizedType);
        assertEquals(Integer.class, genericMaster.getGenericType(field3));

        // Test case 4
        Field field4 = new Field();
        field4.setGenericType(ParameterizedType.class);
        Type[] actualTypeArguments2 = new Type[1];
        actualTypeArguments2[0] = String.class;
        ParameterizedType parameterizedType2 = new ParameterizedType();
        parameterizedType2.setActualTypeArguments(actualTypeArguments2);
        field4.setGenericType(parameterizedType2);
        assertEquals(String.class, genericMaster.getGenericType(field4));
    }
}