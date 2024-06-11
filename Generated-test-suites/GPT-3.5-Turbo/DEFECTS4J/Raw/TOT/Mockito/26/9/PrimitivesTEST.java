import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void primitiveTypeOf_typical() {
        Class<?> result = Primitives.primitiveTypeOf(int.class);
        assertEquals(int.class, result);
    }

    @Test
    public void primitiveTypeOf_edge() {
        Class<?> result = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void primitiveTypeOf_error() {
        Class<?> result = Primitives.primitiveTypeOf(String.class);
        assertNull(result);
    }

    @Test
    public void isPrimitiveWrapper_typical() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result);
    }

    @Test
    public void isPrimitiveWrapper_edge() {
        boolean result = Primitives.isPrimitiveWrapper(Double.class);
        assertFalse(result);
    }

    @Test
    public void isPrimitiveWrapper_error() {
        boolean result = Primitives.isPrimitiveWrapper(Object.class);
        assertFalse(result);
    }

    @Test
    public void primitiveWrapperOf_typical() {
        Integer result = Primitives.primitiveWrapperOf(int.class);
        assertNotNull(result);
    }

    @Test
    public void primitiveWrapperOf_edge() {
        Double result = Primitives.primitiveWrapperOf(double.class);
        assertNotNull(result);
    }

    @Test
    public void primitiveWrapperOf_error() {
        String result = Primitives.primitiveWrapperOf(String.class);
        assertNull(result);
    }

    @Test
    public void primitiveValueOrNullFor_typical() {
        Boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
        assertNotNull(result);
    }

    @Test
    public void primitiveValueOrNullFor_edge() {
        Character result = Primitives.primitiveValueOrNullFor(char.class);
        assertNotNull(result);
    }

    @Test
    public void primitiveValueOrNullFor_error() {
        Object result = Primitives.primitiveValueOrNullFor(Object.class);
        assertNull(result);
    }
}