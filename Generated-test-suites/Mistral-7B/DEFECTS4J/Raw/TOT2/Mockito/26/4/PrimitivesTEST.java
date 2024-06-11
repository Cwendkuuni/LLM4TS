import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> actual = Primitives.primitiveTypeOf(intClass);
        assertEquals(expected, actual);
    }
}