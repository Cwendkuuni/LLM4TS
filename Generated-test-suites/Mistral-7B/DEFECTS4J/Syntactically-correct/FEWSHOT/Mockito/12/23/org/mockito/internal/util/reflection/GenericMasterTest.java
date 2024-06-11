package org.mockito.internal.util.reflection.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class clazz = MyClass.class;
        Field field = getField(clazz, "myField");
        assertEquals(String.class, new GenericMaster().getGenericType(field));

        clazz = NestedClass.class;
        field = getField(clazz, "myNestedField");
        assertEquals(List.class, new GenericMaster().getGenericType(field));

        clazz = NestedNestedClass.class;
        field = getField(clazz, "myNestedNestedField");
        assertEquals(Map.class, new GenericMaster().getGenericType(field));

        clazz = Object.class;
        field = getField(clazz, "serialVersionUID");
        assertNull(new GenericMaster().getGenericType(field));
    }

    private Field getField(Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyClass {
        private String myField;

        @SuppressWarnings("unused")
        private MyClass() {}

        @SuppressWarnings("unused")
        private MyClass(String field) {
            this.myField = field;
        }
    }

    static class NestedClass<T> {
        private List<T> myNestedField;

        @SuppressWarnings("unused")
        private NestedClass() {}

        @SuppressWarnings("unused")
        private NestedClass(List<T> field) {
            this.myNestedField = field;
        }
    }

    static class NestedNestedClass<K, V> {
        private Map<K, V> myNestedNestedField;

        @SuppressWarnings("unused")
        private NestedNestedClass() {}

        @SuppressWarnings("unused")
        private NestedNestedClass(Map<K, V> field) {
            this.myNestedNestedField = field;
        }
    }
}