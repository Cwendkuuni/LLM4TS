package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private Class<?> clazz;
 private Class<?> wrapperClass;
 private Object primitiveValue;

 @Before
 public void setup() {
 this.clazz = Integer.class;
 this.wrapperClass = Integer.class;
 this.primitiveValue = 0;
 }

 @Test
 public void testPrimitiveTypeOf() {
 assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
 assertNull(Primitives.primitiveTypeOf(String.class));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 assertFalse(Primitives.isPrimitiveWrapper(String.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
 assertNull(Primitives.primitiveWrapperOf(String.class));
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 assertEquals('\u0000', Primitives.primitiveValueOrNullFor(char.class));
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(short.class));
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 assertEquals(0, Primitives.primitiveValueOrNullFor(int.class));
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 assertEquals(0L, Primitives.primitiveValueOrNullFor(long.class));
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 assertEquals(0F, Primitives.primitiveValueOrNullFor(float.class));
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 assertEquals(0D, Primitives.primitiveValueOrNullFor(double.class));
 }

 @Test
 public void testPrimitiveTypeOfWrapper() {
 assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class));
 assertNotNull(Primitives.primitiveTypeOf(Integer.class.getWrapperType()));
 assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(Integer.class.getWrapperType()));
 }

 @Test
 public void testIsPrimitiveWrapperWrapper() {
 assertFalse(Primitives.isPrimitiveWrapper(Integer.class.getWrapperType()));
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 }

 @Test
 public void testPrimitiveWrapperOfWrapper() {
 assertNull(Primitives.primitiveWrapperOf(Integer.class.getWrapperType()));
 assertEquals(0, Primitives.primitiveWrapperOf(Integer.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperBoolean() {
 assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperCharacter() {
 assertEquals('\u0000', Primitives.primitiveValueOrNullFor(Character.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperByte() {
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(Byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperShort() {
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(Short.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperInt() {
 assertEquals(0, Primitives.primitiveValueOrNullFor(Integer.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperLong() {
 assertEquals(0L, Primitives.primitiveValueOrNullFor(Long.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperFloat() {
 assertEquals(0F, Primitives.primitiveValueOrNullFor(Float.class));
 }

 @Test
 public void testPrimitiveValueOrNullForWrapperDouble() {
 assertEquals(0D, Primitives.primitiveValueOrNullFor(Double.class));
 }
}