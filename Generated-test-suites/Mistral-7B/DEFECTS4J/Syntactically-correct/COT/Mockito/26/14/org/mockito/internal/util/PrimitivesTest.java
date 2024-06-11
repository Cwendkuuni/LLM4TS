package org.mockito.internal.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = new Class<?>[] {
 Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 Float.class, Double.class };

 @Test
 public void testPrimitiveTypeOf_PrimitiveClass() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Class<?> result = Primitives.primitiveTypeOf(primitiveType);
 assertEquals(primitiveType, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveTypeOf_NonPrimitiveClass() {
 Class<?> nonPrimitiveType = String.class;
 try {
 Primitives.primitiveTypeOf(nonPrimitiveType);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsPrimitiveWrapper_BooleanWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 }

 @Test
 public void testIsPrimitiveWrapper_CharacterWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 }

 @Test
 public void testIsPrimitiveWrapper_ByteWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 }

 @Test
 public void testIsPrimitiveWrapper_ShortWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Short.class));
 }

 @Test
 public void testIsPrimitiveWrapper_IntegerWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 }

 @Test
 public void testIsPrimitiveWrapper_LongWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Long.class));
 }

 @Test
 public void testIsPrimitiveWrapper_FloatWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Float.class));
 }

 @Test
 public void testIsPrimitiveWrapper_DoubleWrapper() {
 assertTrue(Primitives.isPrimitiveWrapper(Double.class));
 }

 @Test
 public void testIsPrimitiveWrapper_NonWrapperClass() {
 assertFalse(Primitives.isPrimitiveWrapper(String.class));
 }

 @Test
 public void testPrimitiveWrapperOf_BooleanWrapper() {
 Boolean result = Primitives.primitiveWrapperOf(Boolean.class);
 assertSame(false, result);
 }

 @Test
 public void testPrimitiveWrapperOf_CharacterWrapper() {
 Character result = Primitives.primitiveWrapperOf(Character.class);
 assertSame('\u0000', result);
 }

 @Test
 public void testPrimitiveWrapperOf_ByteWrapper() {
 Byte result = Primitives.primitiveWrapperOf(Byte.class);
 assertSame((byte) 0, result);
 }

 @Test
 public void testPrimitiveWrapperOf_ShortWrapper() {
 Short result = Primitives.primitiveWrapperOf(Short.class);
 assertSame((short) 0, result);
 }

 @Test
 public void testPrimitiveWrapperOf_IntegerWrapper() {
 Integer result = Primitives.primitiveWrapperOf(Integer.class);
 assertSame(0, result);
 }

 @Test
 public void testPrimitiveWrapperOf_LongWrapper() {
 Long result = Primitives.primitiveWrapperOf(Long.class);
 assertSame(0L, result);
 }

 @Test
 public void testPrimitiveWrapperOf_FloatWrapper() {
 Float result = Primitives.primitiveWrapperOf(Float.class);
 assertSame(0F, result);
 }

 @Test
 public void testPrimitiveWrapperOf_DoubleWrapper() {
 Double result = Primitives.primitiveWrapperOf(Double.class);
 assertSame(0D, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveWrapperOf_NonWrapperClass() {
 Class<?> nonWrapperClass = String.class;
 try {
 Primitives.primitiveWrapperOf(nonWrapperClass);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor_Boolean() {
 Boolean result = Primitives.primitiveValueOrNullFor(boolean.class);
 assertSame(false, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Character() {
 Character result = Primitives.primitiveValueOrNullFor(char.class);
 assertSame('\u0000', result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Byte() {
 Byte result = Primitives.primitiveValueOrNullFor(byte.class);
 assertSame((byte) 0, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Short() {
 Short result = Primitives.primitiveValueOrNullFor(short.class);
 assertSame((short) 0, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Int() {
 Integer result = Primitives.primitiveValueOrNullFor(int.class);
 assertSame(0, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Long() {
 Long result = Primitives.primitiveValueOrNullFor(long.class);
 assertSame(0L, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Float() {
 Float result = Primitives.primitiveValueOrNullFor(float.class);
 assertSame(0F, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_Double() {
 Double result = Primitives.primitiveValueOrNullFor(double.class);
 assertSame(0D, result);
 }

 @Test
 public void testPrimitiveValueOrNullFor_NonPrimitiveClass() {
 try {
 Primitives.primitiveValueOrNullFor(String.class);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}