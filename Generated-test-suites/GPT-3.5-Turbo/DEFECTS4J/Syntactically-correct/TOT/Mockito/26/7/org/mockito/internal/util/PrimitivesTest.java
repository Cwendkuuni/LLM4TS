import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType_returnsSameType() {
        Class<Boolean> result = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperType_returnsPrimitiveType() {
        Class<Integer> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_nonPrimitiveType_returnsNull() {
        Class<String> result = Primitives.primitiveTypeOf(String.class);
        assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveWrapper_returnsTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_nonPrimitiveWrapper_returnsFalse() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveType_returnsWrapperType() {
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testPrimitiveWrapperOf_nonPrimitiveType_returnsNull() {
        Integer result = Primitives.primitiveWrapperOf(String.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveType_returnsNull() {
        Integer result = Primitives.primitiveValueOrNullFor(int.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_nonPrimitiveType_returnsNull() {
        Integer result = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_wrapperType_returnsPrimitiveValue() {
        int result = Primitives.primitiveValueOrNullFor(Integer.class);
        assertEquals(0, result);
    }
}