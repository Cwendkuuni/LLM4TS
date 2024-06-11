import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.TYPE));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(Byte.TYPE));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.TYPE));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(Short.TYPE));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.TYPE));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(Long.TYPE));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(Float.TYPE));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(Double.TYPE));
        
        assertNull(Primitives.primitiveTypeOf(String.class));
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        assertEquals(false, Primitives.primitiveWrapperOf(Boolean.class));
        assertEquals((byte) 0, Primitives.primitiveWrapperOf(Byte.class));
        assertEquals('\u0000', Primitives.primitiveWrapperOf(Character.class));
        assertEquals((short) 0, Primitives.primitiveWrapperOf(Short.class));
        assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
        assertEquals(0L, Primitives.primitiveWrapperOf(Long.class));
        assertEquals(0F, Primitives.primitiveWrapperOf(Float.class));
        assertEquals(0D, Primitives.primitiveWrapperOf(Double.class), 0.01);
        
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.TYPE));
        assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.TYPE));
        assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.TYPE));
        assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.TYPE));
        assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.TYPE));
        assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.TYPE));
        assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.TYPE));
        assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.TYPE), 0.01);
        
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}