import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
    @Test
    public void testPrimitiveTypeOf() {
        // Test with primitive types
        Class<Boolean> booleanType = Boolean.TYPE;
        Class<Boolean> booleanWrappedType = Boolean.class;
        Class<Byte> byteType = Byte.TYPE;
        Class<Byte> byteWrappedType = Byte.class;
        // Test with wrapper types
        Class<Integer> intType = Integer.TYPE;
        Class<Integer> intWrappedType = Integer.class;
        // Test with custom classes
        Class<String> stringType = String.class;
        Class<String> stringWrappedType = String.class;

        assertEquals(booleanType, Primitives.primitiveTypeOf(booleanWrappedType));
        assertEquals(byteType, Primitives.primitiveTypeOf(byteWrappedType));
        assertEquals(intType, Primitives.primitiveTypeOf(intWrappedType));
        assertEquals(stringType, Primitives.primitiveTypeOf(stringWrappedType));
        assertNull(Primitives.primitiveTypeOf(intType));
    }
}