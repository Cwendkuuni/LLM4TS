import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    // 

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass_ReturnsSameClass() {
        Class<Boolean> clazz = boolean.class;
        Class<Boolean> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(clazz, result);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass_ReturnsPrimitiveType() {
        Class<Boolean> clazz = Boolean.class;
        Class<Boolean> result = Primitives.primitiveTypeOf(clazz);
        assertEquals(boolean.class, result);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveWrapperClass_ReturnsTrue() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
    }

    @Test
    public void testIsPrimitiveWrapper_NonWrapperClass_ReturnsFalse() {
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveType_ReturnsWrapperObject() {
        Class<Boolean> type = boolean.class;
        Boolean result = Primitives.primitiveWrapperOf(type);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType_ReturnsPrimitiveValue() {
        Class<Boolean> primitiveType = boolean.class;
        Boolean result = Primitives.primitiveValueOrNullFor(primitiveType);
        assertEquals(false, result);
    }

    // 
}