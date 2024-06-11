
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        assertEquals(boolean.class, booleanClass);

        Class<Integer> integerClass = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(int.class, integerClass);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        boolean isBooleanWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        assertTrue(isBooleanWrapper);

        boolean isStringWrapper = Primitives.isPrimitiveWrapper(String.class);
        assertFalse(isStringWrapper);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(Boolean.FALSE, booleanWrapper);

        Integer integerWrapper = Primitives.primitiveWrapperOf(int.class);
        assertEquals(Integer.valueOf(0), integerWrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean nullBoolean = Primitives.primitiveValueOrNullFor(boolean.class);
        assertNull(nullBoolean);

        Character nullCharacter = Primitives.primitiveValueOrNullFor(char.class);
        assertNull(nullCharacter);
    }
}
