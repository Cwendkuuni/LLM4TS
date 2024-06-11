package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

import org.junit.Test;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_toStringMethod() {
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMock()).thenReturn(new Object());

        String expected = "Mock for Object, hashCode: " + System.identityHashCode(new Object());
        String result = (String) returnsEmptyValues.answer(invocation);

        assertEquals(expected, result);
    }

    @Test
    public void testAnswer_compareToMethod() {
        Integer mockedObject = mock(Integer.class);

        int result = (Integer) returnsEmptyValues.answer(new MethodCall(Object.class, "compareTo", Integer.class, mockedObject));

        assertEquals(1, result.intValue());
    }

    @Test
    public void testAnswer_primitiveTypes() {
        InvocationOnMock invocation = mock(Byte.class);

        byte result = (Byte) returnsEmptyValues.answer(invocation);

        assertEquals((byte) 0, result);
    }

    @Test
    public void testAnswer_emptyCollection() {
        InvocationOnMock invocation = mock(Collection.class);

        Collection<?> result = (Collection<?>) returnsEmptyValues.answer(invocation);

        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_emptySet() {
        InvocationOnMock invocation = mock(Set.class);

        Set<?> result = (Set<?>) returnsEmptyValues.answer(invocation);

        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_emptyMap() {
        InvocationOnMock invocation = mock(Map.class);

        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);

        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_otherTypes() {
        InvocationOnMock invocation = mock(Object.class);

        Object result = returnsEmptyValues.answer(invocation);

        assertNull(result);
    }

    private static class MethodCall {
        private final Class<?> clazz;
        private final String methodName;
        private final Class<?> returnType;
        private final Object argument;

        public MethodCall(Class<?> clazz, String methodName, Class<?> returnType, Object argument) {
            this.clazz = clazz;
            this.methodName = methodName;
            this.returnType = returnType;
            this.argument = argument;
        }

        public Class<?> getMethod() {
            try {
                return clazz.getMethod(methodName, returnType.isPrimitive() ? new Class[0] : new Class[] {argument.getClass()});
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}