import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_primitiveType() {
        Class<?> result = Primitives.primitiveTypeOf(int.class);
        assertEquals(int.class, result);
    }

    @Test
    public void testPrimitiveTypeOf_wrapperType() {
        Class<?> result = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_primitiveType() {
        boolean result = Primitives.isPrimitiveWrapper(int.class);
        assertFalse(result);
    }

    @Test
    public void testIsPrimitiveWrapper_wrapperType() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertTrue(result);
    }

    @Test
    public void testPrimitiveWrapperOf_primitiveType() {
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertNull(result);
    }

    @Test
    public void testPrimitiveWrapperOf_wrapperType() {
        Integer result = Primitives.primitiveWrapperOf(Integer.class);
        assertEquals(0, result.intValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor_primitiveType() {
        Integer result = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, result.intValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor_wrapperType() {
        Integer result = Primitives.primitiveValueOrNullFor(Integer.class);
        assertNull(result);
    }
}