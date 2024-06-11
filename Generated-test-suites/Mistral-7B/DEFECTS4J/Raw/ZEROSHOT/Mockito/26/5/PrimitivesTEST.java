package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?> clazz;
 private Class<?> expectedClass;
 private boolean isPrimitiveWrapper;
 private Object wrapperValue;
 private T primitiveValue;

 @Test
 public void testPrimitiveTypeOf() {
 clazz = Boolean.class;
 expectedClass = Boolean.TYPE;
 assertSame(expectedClass, Primitives.primitiveTypeOf(clazz));

 clazz = Integer.class;
 expectedClass = Integer.TYPE;
 assertSame(expectedClass, Primitives.primitiveTypeOf(clazz));

 clazz = String.class;
 assertNull(Primitives.primitiveTypeOf(clazz));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 clazz = Boolean.class;
 isPrimitiveWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertTrue(isPrimitiveWrapper);

 clazz = Integer.class;
 isPrimitiveWrapper = Primitives.isPrimitiveWrapper(clazz);
 assertFalse(isPrimitiveWrapper);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 clazz = Boolean.class;
 wrapperValue = Primitives.primitiveWrapperOf(clazz);
 assertSame(false, wrapperValue);

 clazz = Byte.class;
 wrapperValue = Primitives.primitiveWrapperOf(clazz);
 assertSame((byte) 0, wrapperValue);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 primitiveValue = Primitives.primitiveValueOrNullFor(boolean.class);
 assertSame(false, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 primitiveValue = Primitives.primitiveValueOrNullFor(char.class);
 assertSame('\u0000', primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 primitiveValue = Primitives.primitiveValueOrNullFor(byte.class);
 assertSame((byte) 0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 primitiveValue = Primitives.primitiveValueOrNullFor(short.class);
 assertSame((short) 0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 primitiveValue = Primitives.primitiveValueOrNullFor(int.class);
 assertSame(0, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 primitiveValue = Primitives.primitiveValueOrNullFor(long.class);
 assertSame(0L, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 primitiveValue = Primitives.primitiveValueOrNullFor(float.class);
 assertSame(0F, primitiveValue);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 primitiveValue = Primitives.primitiveValueOrNullFor(double.class);
 assertSame(0D, primitiveValue);
 }
}