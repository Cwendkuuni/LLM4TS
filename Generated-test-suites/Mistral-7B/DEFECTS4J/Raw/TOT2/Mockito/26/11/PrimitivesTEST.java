import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> actual = Primitives.primitiveTypeOf(integerClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOfWithNonPrimitiveClass() {
        Class<String> stringClass = String.class;
        Primitives.primitiveTypeOf(stringClass); // should throw an IllegalArgumentException
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Class<Boolean> booleanPrimitiveClass = Boolean.TYPE;
        boolean expected = true;
        boolean actual = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPrimitiveWrapperWithNonWrapperClass() {
        Class<Integer> integerClass = Integer.class;
        boolean expected = false;
        boolean actual = Primitives.isPrimitiveWrapper(integerClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean expected = false;
        Boolean actual = Primitives.primitiveWrapperOf(booleanWrapperClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOfWithNonWrapperClass() {
        Class<Integer> integerClass = Integer.class;
        Primitives.primitiveWrapperOf(integerClass); // should throw an IllegalArgumentException
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanPrimitiveClass = Boolean.TYPE;
        Boolean expected = false;
        Boolean actual = Primitives.primitiveValueOrNullFor(booleanPrimitiveClass);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testPrimitiveValueOrNullForWithNullClass() {
        Primitives.primitiveValueOrNullFor(null); // should throw a NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveValueOrNullForWithNonExistentPrimitiveType() {
        Class<String> stringClass = String.class;
        Primitives.primitiveValueOrNullFor(stringClass); // should throw an IllegalArgumentException
    }
}