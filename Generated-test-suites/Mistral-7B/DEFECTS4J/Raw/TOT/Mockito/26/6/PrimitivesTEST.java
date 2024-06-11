package org.mockito.internal.util.test;

import org.junit.Test;
import org.junit.Assert.*;

public class PrimitivesTest {

// Test cases for primitiveTypeOf method
// ...

// Test cases for isPrimitiveWrapper method
// ...

// Test cases for primitiveWrapperOf method
// ...

// Test cases for primitiveValueOrNullFor method
// ...

// Test case for primitiveValueOrNullFor with null argument
@Test(expected = NullPointerException.class)
public void testPrimitiveValueOrNullForNull() {
    Class<String> stringClass = String.class;

    Primitives.primitiveValueOrNullFor(null);
}
}