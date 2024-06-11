import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<String> stringClass = String.class;
        Class<String> expectedStringType = String.class;

        // Test with primitive type
        Class<Integer> actualIntType = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedIntType, actualIntType);

        // Test with non-primitive type
        Class<String> actualStringType = Primitives.primitiveTypeOf(stringClass);
        assertEquals(expectedStringType, actualStringType);
    }
}