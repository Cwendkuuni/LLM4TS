package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    @Test
    public void testPrimitiveTypeOf() {
        // Test typical use case
        Class<Integer> clazz = Primitives.primitiveTypeOf(int.class);
        Assert.assertEquals(Integer.TYPE, clazz);

        // Test with wrapper class
        Class<Integer> clazz2 = Primitives.primitiveTypeOf(Integer.class);
        Assert.assertNull(clazz2);
    }

    @Test
    public void testIsPrimitiveWrapper() {
        // Test typical use case
        boolean isWrapper = Primitives.isPrimitiveWrapper(Integer.class);
        Assert.assertFalse(isWrapper);

        // Test with primitive type
        boolean isWrapper2 = Primitives.isPrimitiveWrapper(int.class);
        Assert.assertTrue(isWrapper2);
    }

    @Test
    public void testPrimitiveWrapperOf() {
        // Test typical use case
        Integer wrapper = Primitives.primitiveWrapperOf(int.class);
        Assert.assertNull(wrapper);

        // Test with wrapper class
        Integer wrapper2 = Primitives.primitiveWrapperOf(Integer.class);
        Assert.assertEquals(0, wrapper2.intValue());
    }

    @Test
    public void testPrimitiveValueOrNullFor() {
        // Test typical use case
        Integer value = Primitives.primitiveValueOrNullFor(Integer.TYPE);
        Assert.assertNull(value);

        // Test with primitive type
        Integer value2 = Primitives.primitiveValueOrNullFor(int.class);
        Assert.assertEquals(0, value2.intValue());
    }

}