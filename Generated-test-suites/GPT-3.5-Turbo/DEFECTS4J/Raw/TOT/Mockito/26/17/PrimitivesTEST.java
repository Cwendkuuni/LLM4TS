
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass_ReturnsClass() {
        Class<?> result = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass_ReturnsPrimitiveType() {
        Class<?> result = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_InvalidClass_ReturnsNull() {
        Class<?> result = Primitives.primitiveTypeOf(String.class);
        assertNull(result);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass_ReturnsTrue() {
        boolean result = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_NonPrimitiveWrapperClass_ReturnsFalse() {
        boolean result = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveClass_ReturnsWrapperInstance() {
        Object result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testPrimitiveWrapperOf_WrapperClass_ReturnsWrapperInstance() {
        Object result = Primitives.primitiveWrapperOf(Double.class);
        assertEquals(Double.class, result.getClass());
    }

    @Test
    public void testPrimitiveWrapperOf_InvalidClass_ReturnsNull() {
        Object result = Primitives.primitiveWrapperOf(String.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveClass_ReturnsPrimitiveValue() {
        int result = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_WrapperClass_ReturnsPrimitiveValue() {
        byte result = Primitives.primitiveValueOrNullFor(Byte.class);
        assertEquals((byte) 0, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_InvalidClass_ReturnsNull() {
        Object result = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(result);
    }
}
