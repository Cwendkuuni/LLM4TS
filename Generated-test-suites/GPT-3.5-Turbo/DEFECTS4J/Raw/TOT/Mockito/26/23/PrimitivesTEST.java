import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass_ReturnsSameClass() {
        Class<Integer> clazz = int.class;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(clazz, result);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass_ReturnsPrimitiveClass() {
        Class<Integer> clazz = Integer.class;
        Class<Integer> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(int.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass_ReturnsTrue() {
        Class<Integer> clazz = Integer.class;
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveWrapperClass_ReturnsFalse() {
        Class<String> clazz = String.class;
        boolean result = Primitives.isPrimitiveWrapper(clazz);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveClass_ReturnsWrapperObject() {
        Class<Integer> clazz = int.class;
        Integer expected = 0;
        Integer result = Primitives.primitiveWrapperOf(clazz);
        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveWrapperOf_WrapperClass_ReturnsWrapperObject() {
        Class<Integer> clazz = Integer.class;
        Integer expected = 0;
        Integer result = Primitives.primitiveWrapperOf(clazz);
        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsPrimitiveValue() {
        Class<Integer> clazz = int.class;
        Integer expected = 0;
        Integer result = Primitives.primitiveValueOrNullFor(clazz);
        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_NonPrimitiveType_ReturnsNull() {
        Class<String> clazz = String.class;
        String result = Primitives.primitiveValueOrNullFor(clazz);
        assertNull(result);
    }
}