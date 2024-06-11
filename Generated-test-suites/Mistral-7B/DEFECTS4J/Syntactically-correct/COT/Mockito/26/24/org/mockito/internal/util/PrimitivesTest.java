package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        Class<String> stringClass = String.class;
        Class<Boolean> booleanClass = Boolean.class;
        Class<Byte> byteClass = Byte.class;
        Class<Short> shortClass = Short.class;
        Class<Integer> intClass = Integer.class;
        Class<Long> longClass = Long.class;
        Class<Float> floatClass = Float.class;
        Class<Double> doubleClass = Double.class;

        Class<Boolean> expectedBooleanType = Boolean.TYPE;
        Class<Byte> expectedByteType = Byte.TYPE;
        Class<Short> expectedShortType = Short.TYPE;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<Long> expectedLongType = Long.TYPE;
        Class<Float> expectedFloatType = Float.TYPE;
        Class<Double> expectedDoubleType = Double.TYPE;

        for (Class<?> type : new Class[]{stringClass, booleanClass, byteClass, shortClass, intClass, longClass, floatClass, doubleClass}) {
            Class<?> result = Primitives.primitiveTypeOf(type);
            if (type.isPrimitive()) {
                assertEquals(type, result);
            } else {
                Assert.assertNotNull(result);
                Assert.assertEquals(expectedTypeFor(type), result);
            }
        }
    }

    private Class<?> expectedTypeFor(Class<?> type) {
        if (type == Boolean.TYPE) {
            return Boolean.class;
        } else if (type == Byte.TYPE) {
            return Byte.class;
        } else if (type == Short.TYPE) {
            return Short.class;
        } else if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        }
        throw new IllegalStateException("Unexpected primitive type: " + type.getName());
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<?> booleanWrapperType = Boolean.class;
        Class<?> charWrapperType = Character.class;
        Class<?> byteWrapperType = Byte.class;
        Class<?> shortWrapperType = Short.class;
        Class<?> intWrapperType = Integer.class;
        Class<?> longWrapperType = Long.class;
        Class<?> floatWrapperType = Float.class;
        Class<?> doubleWrapperType = Double.class;

        for (Class<?> type : new Class[]{booleanWrapperType, charWrapperType, byteWrapperType, shortWrapperType, intWrapperType, longWrapperType, floatWrapperType, doubleWrapperType}) {
            boolean result = Primitives.isPrimitiveWrapper(type);
            assertTrue(result);
        }

        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        boolean result = Primitives.isPrimitiveWrapper(stringClass);
        assertFalse(result);
        result = Primitives.isPrimitiveWrapper(objectClass);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        for (Map.Entry<Class<?>, Object> primitiveValue : Primitives.primitiveValues.entrySet()) {
            Class<?> primitiveType = primitiveValue.getKey();
            Object expected = primitiveValue.getValue();

            Object result = Primitives.primitiveWrapperOf(primitiveType);
            assertEquals(expected, result);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveWrapperOfNullClass() {
        Primitives.primitiveWrapperOf(null);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        for (Map.Entry<Class<?>, Object> primitiveValue : Primitives.primitiveValues.entrySet()) {
            Class<?> primitiveType = primitiveValue.getKey();
            Object expected = primitiveValue.getValue();

            Object result = Primitives.primitiveValueOrNullFor(primitiveType);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testPrimitiveValueOrNullForNullClass() {
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }
}