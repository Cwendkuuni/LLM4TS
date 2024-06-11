package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class GenericMasterTest {

    GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType_shouldReturnObjectClass_whenFieldIsNotGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("nonGenericField");
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, actual);
    }

    @Test
    public void testGetGenericType_shouldReturnGenericType_whenFieldIsGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("genericField");
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(String.class, actual);
    }

    @Test
    public void testGetGenericType_shouldReturnNestedGenericType_whenFieldIsNestedGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("nestedGenericField");
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(ArrayList.class, actual);
    }

    @Test
    public void testGetGenericType_shouldHandleNullGenericType_whenFieldIsNullGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("nullGenericField");
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, actual);
    }

    @Test
    public void testGetGenericType_shouldHandleEmptyActualTypeArguments_whenFieldIsGenericWithNoArguments() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("emptyGenericField");
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, actual);
    }

    private class DummyClass {
        private String nonGenericField;
        private List<String> genericField;
        private List<ArrayList<String>> nestedGenericField;
        private List<?> nullGenericField;
        private List emptyGenericField;
    }

}

