
import org.junit.Test;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<?> booleanType = Primitives.primitiveTypeOf(boolean.class);
        assertSame(boolean.class, booleanType);

        Class<?> booleanWrapperType = Primitives.primitiveTypeOf(Boolean.class);
        assertSame(boolean.class, booleanWrapperType);

        Class<?> intWrapperType = Primitives.primitiveTypeOf(Integer.class);
        assertSame(int.class, intWrapperType);

        Class<?> stringType = Primitives.primitiveTypeOf(String.class);
        assertNull(stringType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
        assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
        assertFalse(Primitives.isPrimitiveWrapper(String.class));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Boolean booleanWrapper = Primitives.primitiveWrapperOf(boolean.class);
        assertNull(booleanWrapper);

        Integer intWrapper = Primitives.primitiveWrapperOf(int.class);
        assertNull(intWrapper);

        Double doubleWrapper = Primitives.primitiveWrapperOf(Double.class);
        assertEquals(Double.class, doubleWrapper.getClass());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(false, booleanValue);

        Integer intValue = Primitives.primitiveValueOrNullFor(int.class);
        assertEquals(0, intValue.intValue());

        Character charValue = Primitives.primitiveValueOrNullFor(char.class);
        assertEquals('\u0000', charValue.charValue());

        String stringValue = Primitives.primitiveValueOrNullFor(String.class);
        assertNull(stringValue);
    }
    
}
