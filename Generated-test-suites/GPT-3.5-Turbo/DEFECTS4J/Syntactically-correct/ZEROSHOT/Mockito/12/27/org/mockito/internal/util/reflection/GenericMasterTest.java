
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setup() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("genericField");

        Class result = genericMaster.getGenericType(field);

        assertNotNull(result);
        assertEquals(MyClass.class, result);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("nonGenericField");

        Class result = genericMaster.getGenericType(field);

        assertNotNull(result);
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("nestedGenericField");

        Class result = genericMaster.getGenericType(field);

        assertNotNull(result);
        assertEquals(NestedClass.class, result);
    }

    private class MyClass<T> {
        public T genericField;
        public int nonGenericField;
        public MyNestedClass<MyClass<NestedClass>> nestedGenericField;
    }

    private class NestedClass {
    }

    private class MyNestedClass<T> {
    }
}
