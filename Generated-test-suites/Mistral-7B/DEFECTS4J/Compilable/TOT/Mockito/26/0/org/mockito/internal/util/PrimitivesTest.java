package org.mockito.internal.util.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Integer> integerClass = Integer.class;
        Class<Double> doubleClass = Double.class;

        Class<Boolean> expectedBoolean = Boolean.TYPE;
        Class<Integer> expectedInteger = Integer.TYPE;
        Class<Double> expectedDouble = Double.TYPE;

        Class<?> actualBoolean = Primitives.primitiveTypeOf(booleanClass);
        Class<?> actualInteger = Primitives.primitiveTypeOf(integerClass);
        Class<?> actualDouble = Primitives.primitiveTypeOf(doubleClass);

        assertEquals(expectedBoolean, actualBoolean);
        assertEquals(expectedInteger, actualInteger);
        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Class<Integer> integerClass = Integer.class;
        Class<String> stringClass = String.class;

        boolean booleanIsWrapper = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        boolean integerIsWrapper = Primitives.isPrimitiveWrapper(integerClass);
        boolean stringIsWrapper = Primitives.isPrimitiveWrapper(stringClass);

        assertTrue(booleanIsWrapper);
        assertFalse(integerIsWrapper);
        assertFalse(stringIsWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Integer> integerClass = Integer.class;
        Class<Double> doubleClass = Double.class;

        Boolean expectedBoolean = false;
        Integer expectedInteger = 0;
        Double expectedDouble = 0.0;

        Object actualBoolean = Primitives.primitiveWrapperOf(booleanClass);
        Object actualInteger = Primitives.primitiveWrapperOf(integerClass);
        Object actualDouble = Primitives.primitiveWrapperOf(doubleClass);

        if (actualBoolean instanceof Boolean) {
            assertEquals(expectedBoolean, (Boolean) actualBoolean);
        } else {
            fail("expected a Boolean, but got: " + actualBoolean.getClass().getName());
        }

        if (actualInteger instanceof Integer) {
            assertEquals(expectedInteger, (Integer) actualInteger);
        } else {
            fail("expected an Integer, but got: " + actualInteger.getClass().getName());
        }

        if (actualDouble instanceof Double) {
            assertEquals(expectedDouble, (Double) actualDouble);
        } else {
            fail("expected a Double, but got: " + actualDouble.getClass().getName());
        }
    }
}