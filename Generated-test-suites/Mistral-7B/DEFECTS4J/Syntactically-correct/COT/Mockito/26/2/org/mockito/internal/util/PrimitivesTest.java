import org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Boolean> expected = Boolean.TYPE;
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expectedInt = Integer.TYPE;

        Assert.assertEquals(expected, Primitives.primitiveTypeOf(booleanClass));
        Assert.assertEquals(expectedInt, Primitives.primitiveTypeOf(integerClass));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOfNull() {
        Class<?> nullClass = null;
        Primitives.primitiveTypeOf(nullClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Class<Boolean> booleanPrimitiveClass = Boolean.TYPE;
        Class<Integer> integerWrapperClass = Integer.class;
        Class<Integer> integerPrimitiveClass = Integer.TYPE;

        Assert.assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
        Assert.assertTrue(Primitives.isPrimitiveWrapper(integerWrapperClass));
        Assert.assertFalse(Primitives.isPrimitiveWrapper(booleanPrimitiveClass));
        Assert.assertFalse(Primitives.isPrimitiveWrapper(integerPrimitiveClass));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean expected = false;

        Assert.assertEquals(expected, Primitives.primitiveWrapperOf(booleanClass));

        Class<Character> charClass = Character.class;
        Character expectedChar = '\u0000';

        Assert.assertEquals(expectedChar, Primitives.primitiveWrapperOf(charClass));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfNull() {
        Class<?> nullClass = null;
        Primitives.primitiveWrapperOf(nullClass);
    }

    @Test
    public void testPrimitiveValueOrNullForBoolean() {
        Assert.assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
    }

    @Test
    public void testPrimitiveValueOrNullForChar() {
        Assert.assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
    }

    @Test
    public void testPrimitiveValueOrNullForByte() {
        Assert.assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
    }

    @Test
    public void testPrimitiveValueOrNullForShort() {
        Assert.assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
    }

    @Test
    public void testPrimitiveValueOrNullForInt() {
        Assert.assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
    }

    @Test
    public void testPrimitiveValueOrNullForLong() {
        Assert.assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
    }

    @Test
    public void testPrimitiveValueOrNullForFloat() {
        Assert.assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
    }

    @Test
    public void testPrimitiveValueOrNullForDouble() {
        Assert.assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
    }

    @Test
    public void testStaticInitialization() {
        Map<Class<?>, Class<?>> primitiveTypesMap = Primitives.primitiveTypes;
        Map<Class<?>, Object> wrapperReturnValuesMap = Primitives.wrapperReturnValues;
        Map<Class<?>, Object> primitiveValuesMap = Primitives.primitiveValues;

        Assert.assertEquals(primitiveTypes.size(), primitiveTypesMap.size());
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveTypes.entrySet()) {
            Assert.assertEquals(entry.getValue(), primitiveTypesMap.get(entry.getKey()));
        }

        Assert.assertEquals(wrapperReturnValues.size(), wrapperReturnValuesMap.size());
        for (Map.Entry<Class<?>, Object> entry : wrapperReturnValues.entrySet()) {
            if (entry.getKey().isPrimitive()) {
                Assert.fail("Wrapper return value for primitive type: " + entry.getKey().getName());
            }
            Assert.assertEquals(entry.getValue(), wrapperReturnValuesMap.get(entry.getKey()));
        }

        Assert.assertEquals(primitiveValues.size(), primitiveValuesMap.size());
        for (Map.Entry<Class<?>, Object> entry : primitiveValues.entrySet()) {
            if (!entry.getKey().isPrimitive()) {
                Assert.fail("Primitive value for non-primitive type: " + entry.getKey().getName());
            }
            Assert.assertEquals(entry.getValue(), primitiveValuesMap.get(entry.getKey()));
        }
    }
}