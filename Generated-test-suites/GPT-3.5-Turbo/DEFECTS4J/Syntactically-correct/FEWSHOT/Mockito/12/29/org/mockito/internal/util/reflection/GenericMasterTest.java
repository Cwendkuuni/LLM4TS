package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("genericField");
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(String.class, actualType);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("nonGenericField");
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(null);
        Assert.assertEquals(Object.class, actualType);
    }

    private class DummyClass {
        private List<String> genericField;
        private String nonGenericField;
    }
}