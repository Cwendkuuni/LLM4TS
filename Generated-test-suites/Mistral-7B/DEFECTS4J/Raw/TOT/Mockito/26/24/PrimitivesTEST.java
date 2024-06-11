import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        Class<Integer> intClass = Integer.class;
        Class<Integer> expectedType = Integer.TYPE;
        Class<Integer> actualType = Primitives.primitiveTypeOf(intClass);
        org.junit.Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        boolean expectedResult = true;
        boolean actualResult = Primitives.isPrimitiveWrapper(booleanWrapperClass);
        org.junit.Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        Class<Boolean> booleanWrapperClass = Boolean.class;
        Boolean expectedWrapper = false;
        Boolean actualWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
        org.junit.Assert.assertEquals(expectedWrapper, actualWrapper);
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        Class<Boolean> booleanPrimitiveClass = Boolean.TYPE;
        Boolean expectedValue = false;
        Boolean actualValue = Primitives.primitiveValueOrNullFor(booleanPrimitiveClass);
        org.junit.Assert.assertEquals(expectedValue, actualValue);
    }
}