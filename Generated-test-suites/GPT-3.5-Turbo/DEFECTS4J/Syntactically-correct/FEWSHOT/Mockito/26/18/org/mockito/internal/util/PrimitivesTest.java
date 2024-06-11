import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimitivesTest {
    
    @Test
    public void testPrimitiveTypeOf() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(Boolean.class));
        assertEquals(Character.TYPE, Primitives.primitiveTypeOf(Character.class));
        assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(Byte.class));
        assertEquals(Short.TYPE, Primitives.primitiveTypeOf(Short.class));
        assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
        assertEquals(Long.TYPE, Primitives.primitiveTypeOf(Long.class));
        assertEquals(Float.TYPE, Primitives.primitiveTypeOf(Float.class));
        assertEquals(Double.TYPE, Primitives.primitiveTypeOf(Double.class));
        assertNull(Primitives.primitiveTypeOf(String.class));
    }
    
    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Character.class));
        assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
        assertTrue(Primitives.isPrimitiveWrapper(Short.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertTrue(Primitives.isPrimitiveWrapper(Long.class));
        assertTrue(Primitives.isPrimitiveWrapper(Float.class));
        assertTrue(Primitives.isPrimitiveWrapper(Double.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }
    
    @Test
    public void testPrimitiveWrapperOf() {
        assertFalse(Primitives.primitiveWrapperOf(Boolean.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Character.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Byte.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Short.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Integer.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Long.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Float.TYPE));
        assertFalse(Primitives.primitiveWrapperOf(Double.TYPE));
        assertNull(Primitives.primitiveWrapperOf(String.class));
    }
    
    @Test
    public void testPrimitiveValueOrNullFor() {
        assertFalse(Primitives.primitiveValueOrNullFor(Boolean.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Character.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Byte.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Short.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Integer.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Long.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Float.class));
        assertFalse(Primitives.primitiveValueOrNullFor(Double.class));
        assertNull(Primitives.primitiveValueOrNullFor(String.class));
    }
}