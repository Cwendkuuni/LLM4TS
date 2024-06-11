import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf_Primitive_ReturnsSameClass() {
        Class<Boolean> booleanClass = Boolean.TYPE;
        Class<Boolean> result = Primitives.primitiveTypeOf(booleanClass);
        Assert.assertEquals(booleanClass, result);
    }

    @Test
    public void testPrimitiveTypeOf_Wrapper_ReturnsPrimitiveType() {
        Class<Boolean> booleanClass = Boolean.class;
        Class<Boolean> primitiveClass = Boolean.TYPE;
        Class<Boolean> result = Primitives.primitiveTypeOf(booleanClass);
        Assert.assertEquals(primitiveClass, result);
    }

    @Test
    public void testIsPrimitiveWrapper_IsPrimitiveWrapper_ReturnsTrue() {
        Class<?> booleanClass = Boolean.class;
        boolean result = Primitives.isPrimitiveWrapper(booleanClass);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPrimitiveWrapper_IsNotPrimitiveWrapper_ReturnsFalse() {
        Class<?> stringClass = String.class;
        boolean result = Primitives.isPrimitiveWrapper(stringClass);
        Assert.assertFalse(result);
    }

    @Test
    public void testPrimitiveWrapperOf_ReturnsWrapperInstance() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean result = Primitives.primitiveWrapperOf(booleanClass);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_ReturnsPrimitiveValue() {
        Class<Boolean> booleanClass = Boolean.TYPE;
        Boolean result = Primitives.primitiveValueOrNullFor(booleanClass);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testPrimitiveValueOrNullFor_ReturnsNullForWrapper() {
        Class<Boolean> booleanClass = Boolean.class;
        Boolean result = Primitives.primitiveValueOrNullFor(booleanClass);
        Assert.assertNull(result);
    }
}