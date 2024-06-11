import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<?> result = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean result = Primitives.isPrimitiveWrapper(Integer.class);
        assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Object result = Primitives.primitiveWrapperOf(int.class);
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Object result = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveTypesMap() {
        assertEquals(Boolean.TYPE, Primitives.primitiveTypes.get(Boolean.class));
    }

    @Test
    public void testWrapperReturnValuesMap() {
        assertEquals(false, Primitives.wrapperReturnValues.get(Boolean.class));
    }

    @Test
    public void testPrimitiveValuesMap() {
        assertEquals(0, Primitives.primitiveValues.get(int.class));
    }
}