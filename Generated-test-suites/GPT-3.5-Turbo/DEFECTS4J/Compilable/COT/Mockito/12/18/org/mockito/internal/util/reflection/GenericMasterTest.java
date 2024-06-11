package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_nonGenericField_returnsObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField(GenericMaster.class, "nonGenericField");
        Class<?> actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericType_genericField_returnsGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField(GenericMaster.class, "genericField");
        Class<?> actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(String.class, actualType);
    }

    @Test
    public void testGetGenericType_nestedGenericField_returnsGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField(GenericMaster.class, "nestedGenericField");
        Class<?> actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(Integer.class, actualType);
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}