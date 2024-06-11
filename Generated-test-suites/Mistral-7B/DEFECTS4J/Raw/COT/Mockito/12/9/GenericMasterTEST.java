package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class clazz = MyClassWithGenericField.class;
        Field field = null; //assume getDeclaredField("fieldName") call here

        Class genericType = new GenericMaster().getGenericType(field);
        Class expectedGenericType = MyGenericType.class;

        assertEquals(expectedGenericType, genericType);
    }

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeNullField() {
        new GenericMaster().getGenericType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeNonFieldObject() {
        new GenericMaster().getGenericType(new Object());
    }

    @Test
    public void testGetGenericTypeNonParametrizedType() {
        Class clazz = MyNonGenericClass.class;
        Field field = null; //assume getDeclaredField("fieldName") call here

        Class genericType = new GenericMaster().getGenericType(field);
        Class expectedGenericType = Object.class;

        assertEquals(expectedGenericType, genericType);
    }

    @Test
    public void testGetGenericTypeNestedGenerics() {
        Class clazz = MyNestedGenericClass.class;
        Field field = null; //assume getDeclaredField("fieldName") call here

        Class genericType = new GenericMaster().getGenericType(field);
        Class expectedGenericType = MyInnerGenericType.class;

        assertEquals(expectedGenericType, genericType);
    }

    // Test utility class

    private static class MyClassWithGenericField {
        @SuppressWarnings("unused")
        private MyGenericType field;
    }

    private static class MyGenericType {}

    private static class MyNonGenericClass {}

    private static class MyNestedGenericClass<A> {
        private MyInnerGenericType<B> field;
    }

    private static class MyInnerGenericType<B> {}
}