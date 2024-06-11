package org.mockito.internal.util.reflection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

@SuppressWarnings("unchecked")
public class GenericMasterTest {
    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field field = GenericMasterTest.class.getDeclaredField("genericField");
        Class<?> genericClass = genericMaster.getGenericType(field);
        assertEquals(String.class, genericClass);
    }

    @Test
    public void testGetGenericTypeNonGenericField() throws NoSuchFieldException {
        Field field = GenericMasterTest.class.getDeclaredField("nonGenericField");
        Class<?> genericClass = genericMaster.getGenericType(field);
        assertEquals(Object.class, genericClass);
    }

    @Test
    public void testGetGenericTypeNestedGenericField() throws NoSuchFieldException {
        Field field = GenericMasterTest.class.getDeclaredField("nestedGenericField");
        Class<?> genericClass = genericMaster.getGenericType(field);
        assertEquals(Integer.class, genericClass);
    }

    @Test
    public void testGetGenericTypeNoActualTypeArgument() throws NoSuchFieldException {
        Field field = GenericMasterTest.class.getDeclaredField("noActualTypeArgumentField");
        Class<?> genericClass = genericMaster.getGenericType(field);
        assertEquals(Object.class, genericClass);
    }

    private static class GenericField<T> {
        private T value;
    }

    private static class NonGenericField {
        private String value;
    }

    private static class NestedGenericField {
        private GenericField<Integer> value;
    }

    private static class NoActualTypeArgumentField {
        private GenericField value;
    }

    private GenericField<String> genericField;
    private NonGenericField nonGenericField;
    private NestedGenericField nestedGenericField;
    private NoActualTypeArgumentField noActualTypeArgumentField;
}