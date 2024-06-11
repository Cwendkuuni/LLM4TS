import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Testing with null input
        Class<?> nullInput = null;
        assertNull(Primitives.primitiveTypeOf(nullInput));

        Class<Integer> integerClass = Integer.class;
        Class<Integer> intType = Primitives.primitiveTypeOf(integerClass);
        assertTrue(intType.isPrimitive());
        assertEquals(Integer.TYPE, intType);

        Class<String> stringClass = String.class;
        Class<?> stringType = Primitives.primitiveTypeOf(stringClass);
        assertFalse(stringType.isPrimitive());
        assertNull(stringType);

        Class<Object> objectClass = Object.class;
        assertNull(Primitives.primitiveTypeOf(objectClass));

        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        assertNull(Primitives.primitiveTypeOf(stringBufferClass));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
        assertEquals(Boolean.class, Primitives.primitiveTypeOf(booleanWrapperClass));

        Class<Integer> integerClass = Integer.class;
        assertFalse(Primitives.isPrimitiveWrapper(integerClass));
        assertNull(Primitives.primitiveTypeOf(integerClass));

        Class<String> stringClass = String.class;
        assertFalse(Primitives.isPrimitiveWrapper(stringClass));
        assertNull(Primitives.primitiveTypeOf(stringClass));

        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        assertFalse(Primitives.isPrimitiveWrapper(stringBufferClass));
        assertNull(Primitives.primitiveTypeOf(stringBufferClass));

        Class<Object> objectClass = Object.class;
        assertFalse(Primitives.isPrimitiveWrapper(objectClass));
        assertNull(Primitives.primitiveTypeOf(objectClass));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertFalse(booleanWrapper.booleanValue());

        Class<Byte> byteWrapperClass = Byte.class;
        Byte byteWrapper = Primitives.primitiveWrapperOf(byteWrapperClass);
        assertEquals((byte) 0, byteWrapper.byteValue());

        Class<Short> shortWrapperClass = Short.class;
        Short shortWrapper = Primitives.primitiveWrapperOf(shortWrapperClass);
        assertEquals((short) 0, shortWrapper.shortValue());

        Class<Integer> intWrapperClass = Integer.class;
        Integer intWrapper = Primitives.primitiveWrapperOf(intWrapperClass);
        assertEquals(0, intWrapper.intValue());

        Class<Long> longWrapperClass = Long.class;
        Long longWrapper = Primitives.primitiveWrapperOf(longWrapperClass);
        assertEquals(0L, longWrapper.longValue());

        Class<Float> floatWrapperClass = Float.class;
        Float floatWrapper = Primitives.primitiveWrapperOf(floatWrapperClass);
        assertEquals(0F, floatWrapper.floatValue());

        Class<Double> doubleWrapperClass = Double.class;
        Double doubleWrapper = Primitives.primitiveWrapperOf(doubleWrapperClass);
        assertEquals(0D, doubleWrapper.doubleValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
        assertFalse(booleanValue);

        Class<Byte> byteClass = Byte.class;
        Byte byteValue = Primitives.primitiveValueOrNullFor(byteClass);
        assertEquals((byte) 0, byteValue.byteValue());

        Class<Short> shortClass = Short.class;
        Short shortValue = Primitives.primitiveValueOrNullFor(shortClass);
        assertEquals((short) 0, shortValue.shortValue());

        Class<Integer> intClass = Integer.class;
        Integer intValue = Primitives.primitiveValueOrNullFor(intClass);
        assertEquals(0, intValue.intValue());

        Class<Long> longClass = Long.class;
        Long longValue = Primitives.primitiveValueOrNullFor(longClass);
        assertEquals(0L, longValue.longValue());

        Class<Float> floatClass = Float.class;
        Float floatValue = Primitives.primitiveValueOrNullFor(floatClass);
        assertEquals(0F, floatValue.floatValue());

        Class<Double> doubleClass = Double.class;
        Double doubleValue = Primitives.primitiveValueOrNullFor(doubleClass);
        assertEquals(0D, doubleValue.doubleValue());
    }

    @Test
    public void testIsPrimitiveWrapperEdgeCase() {
        Class<Object> objectClass = Object.class;
        assertFalse(Primitives.isPrimitiveWrapper(objectClass));
        assertNull(Primitives.primitiveTypeOf(objectClass));
    }

    @Test
    public void testPrimitiveWrapperOfEdgeCase() {
        Class<Object> objectClass = Object.class;
        assertNull(Primitives.primitiveWrapperOf(objectClass));
    }

    @Test
    public void testPrimitiveValueOrNullForEdgeCase() {
        Class<Object> objectClass = Object.class;
        assertNull(Primitives.primitiveValueOrNullFor(objectClass));
    }
}