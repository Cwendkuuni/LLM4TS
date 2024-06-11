import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedIntType = Integer.TYPE;
        Class<String> stringClass = String.class;
        Class<String> expectedStringType = String.class;

        Class<Integer> result = Primitives.primitiveTypeOf(intClass);
        assertEquals(expectedIntType, result);

        Class<String> resultString = Primitives.primitiveTypeOf(stringClass);
        assertNotSame(expectedIntType, resultString);
        assertEquals(expectedStringType, resultString);
    }
}