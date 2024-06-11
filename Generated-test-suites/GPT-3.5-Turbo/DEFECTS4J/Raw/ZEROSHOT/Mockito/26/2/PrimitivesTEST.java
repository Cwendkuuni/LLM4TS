import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, booleanClass);

        Class<Character> characterClass = Primitives.primitiveTypeOf(Character.class);
        assertEquals(char.class, characterClass);

        Class<Byte> byteClass = Primitives.primitiveTypeOf(Byte.class);
        assertEquals(byte.class, byteClass);

        Class<Short> shortClass = Primitives.primitiveTypeOf(Short.class);
        assertEquals(short.class, shortClass);

        Class<Integer> integerClass = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, integerClass);

        Class<Long> longClass = Primitives.primitiveTypeOf(Long.class);
        assertEquals(long.class, longClass);

        Class<Float> floatClass = Primitives.primitiveTypeOf(Float.class);
        assertEquals(float.class, floatClass);

        Class<Double> doubleClass = Primitives.primitiveTypeOf(Double.class);
        assertEquals(double.class, doubleClass);

        Class<String> stringClass = Primitives.primitiveTypeOf(String.class);
        assertNull(stringClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean isBooleanWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(isBooleanWrapper);

        boolean isCharacterWrapper = Primitives.isPrimitiveWrapper(Character.class);
        assertTrue(isCharacterWrapper);

        boolean isByteWrapper = Primitives.isPrimitiveWrapper(Byte.class);
        assertTrue(isByteWrapper);

        boolean isShortWrapper = Primitives.isPrimitiveWrapper(Short.class);
        assertTrue(isShortWrapper);

        boolean isIntegerWrapper = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(isIntegerWrapper);

        boolean isLongWrapper = Primitives.isPrimitiveWrapper(Long.class);
        assertTrue(isLongWrapper);

        boolean isFloatWrapper = Primitives.isPrimitiveWrapper(Float.class);
        assertTrue(isFloatWrapper);

        boolean isDoubleWrapper = Primitives.isPrimitiveWrapper(Double.class);
        assertTrue(isDoubleWrapper);

        boolean isStringWrapper = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(isStringWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.class, booleanWrapper.getClass());

        Character characterWrapper = Primitives.primitiveWrapperOf(char.class);
        assertEquals(Character.class, characterWrapper.getClass());

        Byte byteWrapper = Primitives.primitiveWrapperOf(byte.class);
        assertEquals(Byte.class, byteWrapper.getClass());

        Short shortWrapper = Primitives.primitiveWrapperOf(short.class);
        assertEquals(Short.class, shortWrapper.getClass());

        Integer integerWrapper = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.class, integerWrapper.getClass());

        Long longWrapper = Primitives.primitiveWrapperOf(long.class);
        assertEquals(Long.class, longWrapper.getClass());

        Float floatWrapper = Primitives.primitiveWrapperOf(float.class);
        assertEquals(Float.class, floatWrapper.getClass());

        Double doubleWrapper = Primitives.primitiveWrapperOf(double.class);
        assertEquals(Double.class, doubleWrapper.getClass());

        String stringWrapper = Primitives.primitiveWrapperOf(String.class);
        assertNull(stringWrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, booleanValue);

        Character characterValue = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', characterValue);

        Byte byteValue = Primitives.primitiveValueOrNullFor(byte.class);
        assertEquals((byte) 0, byteValue);

        Short shortValue = Primitives.primitiveValueOrNullFor(short.class);
        assertEquals((short) 0, shortValue);

        Integer integerValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, integerValue);

        Long longValue = Primitives.primitiveValueOrNullFor(long.class);
        assertEquals(0L, longValue);

        Float floatValue = Primitives.primitiveValueOrNullFor(float.class);
        assertEquals(0F, floatValue, 0);

        Double doubleValue = Primitives.primitiveValueOrNullFor(double.class);
        assertEquals(0, doubleValue, 0);

        String stringValue = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(stringValue);
    }
}