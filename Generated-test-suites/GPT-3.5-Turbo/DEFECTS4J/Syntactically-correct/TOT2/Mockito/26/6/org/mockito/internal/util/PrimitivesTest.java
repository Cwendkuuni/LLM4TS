import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(Boolean.class);
        assertEquals(booleanClass, boolean.class);
        
        Class<Integer> intClass = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(intClass, int.class);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanValue = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(booleanValue, false);

        Character charValue = Primitives.primitiveWrapperOf(char.class);
        assertEquals(charValue, '\u0000');
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(booleanValue, false);

        Integer intValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(intValue, 0);
    }

}