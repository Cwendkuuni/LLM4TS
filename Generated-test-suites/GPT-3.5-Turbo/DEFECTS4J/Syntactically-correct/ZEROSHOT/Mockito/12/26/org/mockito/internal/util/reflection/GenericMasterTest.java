package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_ReturnsObjectClass_WhenFieldIsNotGenericType() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("field");

        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(field);

        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_ReturnsActualTypeArgument_WhenFieldIsGenericType() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("genericField");

        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(field);

        assertEquals(String.class, result);
    }

}