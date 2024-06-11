package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

/**
 * Test class for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerPrimitiveBoolean() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(false, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(boolean.class))));
        assertEquals(false, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Boolean.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveByte() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((byte) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(byte.class))));
        assertEquals((byte) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Byte.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveChar() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((char) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(char.class))));
        assertEquals((char) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Character.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveShort() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((short) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(short.class))));
        assertEquals((short) 0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Short.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveInt() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(int.class))));
        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Integer.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveLong() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0L, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(long.class))));
        assertEquals(0L, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Long.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveFloat() {
       ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0f, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(float.class))));
        assertEquals(0.0f, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Float.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveDouble() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0d, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(double.class))));
        assertEquals(0.0d, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Double.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveBooleanWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Boolean.FALSE, returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Boolean.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveByteWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Byte.valueOf((byte) 0), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Byte.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveCharWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Character.valueOf((char) 0), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Character.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveShortWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Short.valueOf((short) 0), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Short.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveIntWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Integer.valueOf(0), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Integer.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveLongWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Long.valueOf(0L), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Long.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveFloatWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Float.valueOf(0.0f), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Float.TYPE))));
    }

    @Test
    public void testAnswerPrimitiveDoubleWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Double.valueOf(0.0d), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Double.TYPE))));
    }

    @Test
    public void testEmptyList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(new LinkedList<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Collection.class))));
        assertEquals(new ArrayList<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(List.class))));
    }

    @Test
    public void testEmptySet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(new HashSet<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Set.class))));
        assertEquals(new HashSet<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(HashSet.class))));
        assertEquals(new TreeSet<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(SortedSet.class))));
        assertEquals(new TreeSet<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(TreeSet.class))));
        assertEquals(new LinkedHashSet<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(LinkedHashSet.class))));
    }

    @Test
    public void testEmptyMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(new HashMap<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(Map.class))));
        assertEquals(new HashMap<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(HashMap.class))));
        assertEquals(new TreeMap<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(SortedMap.class))));
        assertEquals(new TreeMap<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(TreeMap.class))));
        assertEquals(new LinkedHashMap<>(), returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(LinkedHashMap.class))));
    }

    @Test
    public void testZeroForComparable() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object a = new Object();
        Object b = new Object();
        assertEquals(0, returnsEmptyValues.answer(new InvocationOnMock(a, new MethodStub(Comparable.class))).compareTo(b));
        assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(a, new MethodStub(Comparable.class))).compareTo(a));
    }

    @Test
    public void testNullForOtherMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> type = new Object().getClass();
        assertNull(returnsEmptyValues.answer(new InvocationOnMock(new Object(), new MethodStub(type))));
    }

    private static class MethodStub implements InvocationOnMock {
        private Class<?> returnType;

        public MethodStub(Class<?> returnType) {
            this.returnType = returnType;
        }

        @Override
        public Method getMethod() {
            return new MethodStubMethod(returnType);
        }

        private static class MethodStubMethod implements Method {
            private Class<?> returnType;

            public MethodStubMethod(Class<?> returnType) {
                this.returnType = returnType;
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?> getReturnType() {
                return returnType;
            }
        }
    }
}