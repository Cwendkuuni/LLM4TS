package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.Primitives;

public class PrimitivesTest {
@Test
public void testPrimitiveTypeOf() {
Class<Integer> integerClass = Integer.class;
Class<Integer> expectedIntegerType = Integer.TYPE;

Class<Integer> actualIntegerType = Primitives.primitiveTypeOf(integerClass);

Assert.assertEquals(expectedIntegerType, actualIntegerType);

Class<String> stringClass = String.class;
Class<?> actualStringType = Primitives.primitiveTypeOf(stringClass);
Assert.assertNull(actualStringType);
}

@Test
public void testIsPrimitiveWrapper() {
Class<Boolean> booleanWrapperClass = Boolean.class;
boolean expectedWrapped = true;

Object actualBooleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
Assert.assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
Assert.assertEquals(expectedWrapped, actualBooleanWrapper);

Class<Integer> integerClass = Integer.class;
Assert.assertFalse(Primitives.isPrimitiveWrapper(integerClass));
}

@Test
public void testPrimitiveWrapperOf() {
Class<Boolean> booleanWrapperClass = Boolean.class;
Boolean expectedBoolean = true;

Object actualBooleanWrapper = Primitives.primitiveWrapperOf(booleanWrapperClass);
Assert.assertTrue(actualBooleanWrapper instanceof Boolean);
Assert.assertEquals(expectedBoolean, (Boolean) actualBooleanWrapper);

Class<Integer> integerClass = Integer.class;
Assert.assertNull(Primitives.primitiveWrapperOf(integerClass));
}
}