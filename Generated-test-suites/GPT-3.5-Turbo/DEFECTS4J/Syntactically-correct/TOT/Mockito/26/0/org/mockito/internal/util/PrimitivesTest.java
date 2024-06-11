import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test for primitive type
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> result = Primitives.primitiveTypeOf(int.class);
        assertEquals(expected, result);

        // Test for non-primitive type
        Class<String> expected2 = String.class;
        Class<String> result2 = Primitives.primitiveTypeOf(String.class);
        assertEquals(expected2, result2);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test for primitive wrapper type
        Class<Integer> type = Integer.class;
        assertTrue(Primitives.isPrimitiveWrapper(type));

        // Test for non-primitive wrapper type
        Class<String> type2 = String.class;
        assertFalse(Primitives.isPrimitiveWrapper(type2));
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test for primitive wrapper type
        Class<Integer> type = Integer.class;
        Integer expected = 0;
        Integer result = Primitives.primitiveWrapperOf(type);
        assertEquals(expected, result);

        // Test for non-primitive wrapper type
        Class<String> type2 = String.class;
        assertNull(Primitives.primitiveWrapperOf(type2));
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test for primitive type
        Class<Integer> type = int.class;
        Integer expected = 0;
        Integer result = Primitives.primitiveValueOrNullFor(type);
        assertEquals(expected, result);

        // Test for non-primitive type
        Class<String> type2 = String.class;
        assertNull(Primitives.primitiveValueOrNullFor(type2));
    }

}