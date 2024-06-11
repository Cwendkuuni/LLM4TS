import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
        assertEquals(int.class, Primitives.primitiveTypeOf(Integer.class));
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(Boolean.TRUE, Primitives.primitiveWrapperOf(boolean.class));
        assertEquals(Integer.valueOf(0), Primitives.primitiveWrapperOf(int.class));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
        assertEquals((byte)0, Primitives.primitiveValueOrNullFor(byte.class));
        assertEquals((short)0, Primitives.primitiveValueOrNullFor(short.class));
        assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class), 0.0001);
        assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class), 0.0001);
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }

}