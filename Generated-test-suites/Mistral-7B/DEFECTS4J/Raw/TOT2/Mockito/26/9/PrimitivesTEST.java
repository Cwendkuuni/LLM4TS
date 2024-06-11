import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_PrimitiveClass() {
        Class<Boolean> booleanClass = Boolean.TYPE;
        Class<Boolean> expected = Boolean.TYPE;
        Class<Boolean> actual = Primitives.primitiveTypeOf(booleanClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveTypeOf_WrapperClass() {
        Class<Integer> integerClass = Integer.class;
        Class<Integer> expected = Integer.TYPE;
        Class<Integer> actual = Primitives.primitiveTypeOf(integerClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveTypeOf_NonPrimitiveOrWrapperClass() {
        Class<String> stringClass = String.class;
        Class<String> actual = Primitives.primitiveTypeOf(stringClass);
        fail("Expected IllegalArgumentException but got: " + actual);
    }

    @Test
    public void testIsPrimitiveWrapper_PrimitiveType() {
        Class<Integer> integerType = Integer.TYPE;
        boolean expected = false;
        boolean actual = Primitives.isPrimitiveWrapper(integerType);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPrimitiveWrapper_WrapperType() {
        Class<Integer> integerType = Integer.class;
        boolean expected = true;
        boolean actual = Primitives.isPrimitiveWrapper(integerType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf_PrimitiveType() {
        Class<Boolean> booleanType = Boolean.TYPE;
        Boolean expected = false;
        Boolean actual = Primitives.primitiveWrapperOf(booleanType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveWrapperOf_WrapperType() {
        Class<Integer> integerType = Integer.class;
        Integer expected = 0;
        Integer actual = Primitives.primitiveWrapperOf(integerType);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveWrapperOf_NonPrimitiveOrWrapperType() {
        Class<String> stringType = String.class;
        Object actual = Primitives.primitiveWrapperOf(stringType);
        fail("Expected IllegalArgumentException but got: " + actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_PrimitiveType() {
        Class<Boolean> booleanType = Boolean.TYPE;
        Boolean expected = false;
        Boolean actual = Primitives.primitiveValueOrNullFor(booleanType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_WrapperType() {
        Class<Integer> integerType = Integer.class;
        Integer expected = 0;
        Integer actual = Primitives.primitiveValueOrNullFor(integerType);
        assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveValueOrNullFor_Null() {
        assertNull(Primitives.primitiveValueOrNullFor(null));
    }
}