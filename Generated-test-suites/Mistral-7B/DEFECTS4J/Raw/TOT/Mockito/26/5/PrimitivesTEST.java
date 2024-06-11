import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> integerClazz = Integer.class;
        Class<Integer> expectedIntegerType = Integer.TYPE;
        Class<Integer> actualIntegerType = Primitives.primitiveTypeOf(integerClazz);
        assertEquals(expectedIntegerType, actualIntegerType);
    }

    @Test
    public void testIsPrimitiveWrapper_Boolean() {
        Class<Boolean> booleanClazz = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanClazz);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsPrimitiveWrapper_Integer() {
        Class<Integer> integerClazz = Integer.class;
        boolean expectedResult = false;
        boolean actualResult = Primitives.isPrimitiveWrapper(integerClazz);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testIsPrimitiveWrapper_Null() {
        Class<?> nullClass = null;
        Primitives.isPrimitiveWrapper(nullClass);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanClazz = Boolean.class;
        Object expectedWrapperValue = false;
        Object actualWrapperValue = Primitives.primitiveWrapperOf(booleanClazz);
        assertEquals(expectedWrapperValue, actualWrapperValue);
    }

    @Test
    public void testPrimitiveWrapperOf_Null() {
        Class<?> nullClass = null;
        Object expectedWrapperValue = null;
        Object actualWrapperValue = Primitives.primitiveWrapperOf(nullClass);
        assertNull(actualWrapperValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanClazz = Boolean.class;
        Boolean expectedPrimitiveValue = false;
        Boolean actualPrimitiveValue = Primitives.primitiveValueOrNullFor(booleanClazz);
        assertEquals(expectedPrimitiveValue, actualPrimitiveValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_Null() {
        Class<?> nullClass = null;
        Object expectedPrimitiveValue = null;
        Object actualPrimitiveValue = Primitives.primitiveValueOrNullFor(nullClass);
        assertNull(actualPrimitiveValue);
    }

    @Test
    public void testPrimitiveValueOrNullFor_Long() {
        Class<Long> longClazz = Long.class;
        Long expectedPrimitiveValue = 0L;
        Long actualPrimitiveValue = Primitives.primitiveValueOrNullFor(longClazz);
        assertEquals(expectedPrimitiveValue, actualPrimitiveValue);
    }
}