
import org.junit.Test;
import org.mockito.internal.util.Primitives;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(int.class));
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(char.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.TYPE));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.TYPE));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}
