package org.mockito.internal.util;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {

    // Test for primitiveTypeOf method
    @Test
    public void testPrimitiveTypeOf() {
        Class<Boolean> booleanClass = Primitives.primitiveTypeOf(boolean.class);
        Assert.assertEquals(boolean.class, booleanClass);
        
        Class<Integer> integerClass = Primitives.primitiveTypeOf(Integer.class);
        Assert.assertEquals(int.class, integerClass);
        
        Class<Double> doubleClass = Primitives.primitiveTypeOf(double.class);
        Assert.assertEquals(double.class, doubleClass);
    }

    // Test for isPrimitiveWrapper method
    @Test
    public void testIsPrimitiveWrapper() {
        boolean isWrapper = Primitives.isPrimitiveWrapper(Boolean.class);
        Assert.assertTrue(isWrapper);
        
        boolean isNotWrapper = Primitives.isPrimitiveWrapper(int.class);
        Assert.assertFalse(isNotWrapper);
    }

    // Test for primitiveWrapperOf method
    @Test
    public void testPrimitiveWrapperOf() {
        Integer integerWrapper = Primitives.primitiveWrapperOf(int.class);
        Assert.assertEquals(Integer.class, integerWrapper.getClass());
        
        Character charWrapper = Primitives.primitiveWrapperOf(char.class);
        Assert.assertEquals(Character.class, charWrapper.getClass());
    }

    // Test for primitiveValueOrNullFor method
    @Test
    public void testPrimitiveValueOrNullFor() {
        Boolean booleanValue = Primitives.primitiveValueOrNullFor(boolean.class);
        Assert.assertEquals(false, booleanValue);
        
        Integer integerValue = Primitives.primitiveValueOrNullFor(Integer.class);
        Assert.assertNull(integerValue);
    }
}

// 
// Merge all the individual test cases into a complete JUnit 4 test file (PrimitivesTest.java) for the given Java class.
// The complete JUnit test file must start with  and finish with 
// 