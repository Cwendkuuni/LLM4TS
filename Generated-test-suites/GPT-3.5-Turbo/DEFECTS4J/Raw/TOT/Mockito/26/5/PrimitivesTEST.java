import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Typical use case
        Class<Integer> intClass = Integer.class;
        Class<Integer> result = Primitives.primitiveTypeOf(intClass);
        assertEquals(intClass, result);

        // Edge case
        Class<Long> longClass = Long.class;
        Class<Long> result2 = Primitives.primitiveTypeOf(longClass);
        assertNull(result2);

        // Error scenario
        Class<Object> objectClass = Object.class;
        Class<Object> result3 = Primitives.primitiveTypeOf(objectClass);
        assertNull(result3);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Typical use case
        Class<Integer> intClass = Integer.class;
        boolean result = Primitives.isPrimitiveWrapper(intClass);
        assertFalse(result);

        // Edge case
        Class<Integer> intWrapperClass = Integer.class;
        boolean result2 = Primitives.isPrimitiveWrapper(intWrapperClass);
        assertTrue(result2);

        // Error scenario
        Class<Object> objectClass = Object.class;
        boolean result3 = Primitives.isPrimitiveWrapper(objectClass);
        assertFalse(result3);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Typical use case
        Class<Integer> intClass = Integer.class;
        Integer result = Primitives.primitiveWrapperOf(intClass);
        assertNull(result);

        // Edge case
        Class<Integer> intWrapperClass = Integer.class;
        Integer result2 = Primitives.primitiveWrapperOf(intWrapperClass);
        assertEquals(intClass, result2);

        // Error scenario
        Class<Object> objectClass = Object.class;
        Object result3 = Primitives.primitiveWrapperOf(objectClass);
        assertNull(result3);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Typical use case
        Class<Integer> intClass = Integer.class;
        Integer result = Primitives.primitiveValueOrNullFor(intClass);
        assertNull(result);

        // Edge case
        Class<Integer> intPrimitiveClass = int.class;
        Integer result2 = Primitives.primitiveValueOrNullFor(intPrimitiveClass);
        assertEquals(intClass, result2);

        // Error scenario
        Class<Object> objectClass = Object.class;
        Object result3 = Primitives.primitiveWrapperOf(objectClass);
        assertNull(result3);
    }
}