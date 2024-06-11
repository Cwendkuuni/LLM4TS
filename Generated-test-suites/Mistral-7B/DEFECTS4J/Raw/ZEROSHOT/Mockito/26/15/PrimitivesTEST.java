package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> primitiveType;
 private Class<?> wrapperType;
 private Class<?> expectedWrapperType;
 private Class<?> expectedPrimitiveType;
 private Object wrapperValue;
 private Object primitiveValue;

 @Test
 public void testPrimitiveTypeOf() {
 // Testing Boolean
 primitiveType = Boolean.class;
 expectedPrimitiveType = Boolean.TYPE;
 wrapperType = Boolean.class;
 expectedWrapperType = Boolean.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(wrapperType));

 primitiveType = Boolean.TYPE;
 expectedPrimitiveType = Boolean.TYPE;
 wrapperType = Boolean.class;
 expectedWrapperType = Boolean.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedWrapperType, Primitives.primitiveTypeOf(wrapperType));

 // Testing other primitives
 primitiveType = Character.class;
 expectedPrimitiveType = Character.TYPE;
 wrapperType = Character.class;
 expectedWrapperType = Character.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(wrapperType));

 primitiveType = Character.TYPE;
 expectedPrimitiveType = Character.TYPE;
 wrapperType = Character.class;
 expectedWrapperType = Character.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedWrapperType, Primitives.primitiveTypeOf(wrapperType));

 primitiveType = Byte.class;
 expectedPrimitiveType = Byte.TYPE;
 wrapperType = Byte.class;
 expectedWrapperType = Byte.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(wrapperType));

 primitiveType = Byte.TYPE;
 expectedPrimitiveType = Byte.TYPE;
 wrapperType = Byte.class;
 expectedWrapperType = Byte.class;
 assertSame(expectedPrimitiveType, Primitives.primitiveTypeOf(primitiveType));
 assertSame(expectedWrapperType, Primitives.primitiveTypeOf(wrapperType));

 // Testing other primitives
 // ...
 }

 @Test
 public void testIsPrimitiveWrapper() {
 // Testing Boolean
 primitiveType = Boolean.class;
 assertTrue(Primitives.isPrimitiveWrapper(primitiveType));

 primitiveType = Boolean.TYPE;
 assertFalse(Primitives.isPrimitiveWrapper(primitiveType));

 // Testing other primitives
 primitiveType = Character.class;
 assertFalse(Primitives.isPrimitiveWrapper(primitiveType));

 wrapperType = Character.class;
 assertTrue(Primitives.isPrimitiveWrapper(wrapperType));

 primitiveType = Character.TYPE;
 assertFalse(Primitives.isPrimitiveWrapper(primitiveType));

 wrapperType = Character.TYPE;
 assertFalse(Primitives.isPrimitiveWrapper(wrapperType));

 // Testing other primitives
 // ...
 }

 @Test
 public void testPrimitiveWrapperOf() {
 // Testing Boolean
 primitiveType = Boolean.class;
 expectedWrapperType = Boolean.class;
 wrapperValue = Primitives.primitiveWrapperOf(primitiveType);
 assertSame(expectedWrapperType, wrapperValue.getClass());

 primitiveType = Boolean.TYPE;
 expectedWrapperType = Boolean.class;
 wrapperValue = Primitives.primitiveWrapperOf(primitiveType);
 assertSame(expectedWrapperType, wrapperValue.getClass());

 // Testing other primitives
 // ...
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 // Testing Boolean
 primitiveType = Boolean.class;
 expectedPrimitiveValue = false;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(expectedPrimitiveValue, primitiveValue);

 primitiveType = Boolean.TYPE;
 expectedPrimitiveValue = false;
 primitiveValue = Primitives.primitiveValueOrNullFor(primitiveType);
 assertEquals(expectedPrimitiveValue, primitiveValue);

 // Testing other primitives
 // ...
 }
}