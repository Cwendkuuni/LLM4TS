
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, booleanClass);

        Class<Character> charClass = Primitives.primitiveTypeOf(Character.class);
        assertEquals(char.class, charClass);

        Class<Byte> byteClass = Primitives.primitiveTypeOf(Byte.class);
        assertEquals(byte.class, byteClass);

        Class<Short> shortClass = Primitives.primitiveTypeOf(Short.class);
        assertEquals(short.class, shortClass);

        Class<Integer> intClass = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, intClass);

        Class<Long> longClass = Primitives.primitiveTypeOf(Long.class);
        assertEquals(long.class, longClass);

        Class<Float> floatClass = Primitives.primitiveTypeOf(Float.class);
        assertEquals(float.class, floatClass);

        Class<Double> doubleClass = Primitives.primitiveTypeOf(Double.class);
        assertEquals(double.class, doubleClass);
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
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanValue = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.FALSE, booleanValue);

        Character charValue = Primitives.primitiveWrapperOf(char.class);
        assertEquals(Character.valueOf('\u0000'), charValue);

        Byte byteValue = Primitives.primitiveWrapperOf(byte.class);
        assertEquals(Byte.valueOf((byte) 0), byteValue);

        Short shortValue = Primitives.primitiveWrapperOf(short.class);
        assertEquals(Short.valueOf((short) 0), shortValue);

        Integer intValue = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.valueOf(0), intValue);

        Long longValue = Primitives.primitiveWrapperOf(long.class);
        assertEquals(Long.valueOf(0L), longValue);

        Float floatValue = Primitives.primitiveWrapperOf(float.class);
        assertEquals(Float.valueOf(0F), floatValue);

        Double doubleValue = Primitives.primitiveWrapperOf(double.class);
        assertEquals(Double.valueOf(0D), doubleValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, booleanValue);

        Character charValue = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', charValue.charValue());

        Byte byteValue = Primitives.primitiveValueOrNullFor(byte.class);
        assertEquals((byte) 0, byteValue.byteValue());

        Short shortValue = Primitives.primitiveValueOrNullFor(short.class);
        assertEquals((short) 0, shortValue.shortValue());

        Integer intValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, intValue.intValue());

        Long longValue = Primitives.primitiveValueOrNullFor(long.class);
        assertEquals(0L, longValue.longValue());

        Float floatValue = Primitives.primitiveValueOrNullFor(float.class);
        assertEquals(0F, floatValue.floatValue(), 0);

        Double doubleValue = Primitives.primitiveValueOrNullFor(double.class);
        assertEquals(0D, doubleValue.doubleValue(), 0);
    }
}

