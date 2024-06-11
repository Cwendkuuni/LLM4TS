package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<Integer> intClass = Integer.class;
 Class<Integer> intType = Primitives.primitiveTypeOf(intClass);
 assertTrue(intType.isPrimitive());
 assertEquals(Integer.TYPE, intType);

 Class<String> stringClass = String.class;
 Class<String> stringType = Primitives.primitiveTypeOf(stringClass);
 assertFalse(stringType.isPrimitive());
 assertNull(stringType);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Integer> intClass = Integer.class;
 boolean isWrapper = Primitives.isPrimitiveWrapper(intClass);
 assertFalse(isWrapper);

 Class<Integer> intWrapperClass = Integer.class;
 Class<Integer> intWrapperType = Primitives.primitiveTypeOf(intWrapperClass);
 assertTrue(intWrapperType.isPrimitive());
 isWrapper = Primitives.isPrimitiveWrapper(intWrapperType);
 assertTrue(isWrapper);
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Integer> intClass = Integer.class;
 Integer intWrapper = Primitives.primitiveWrapperOf(intClass);
 assertEquals(0, intWrapper.intValue());

 Class<Boolean> booleanClass = Boolean.class;
 Boolean booleanWrapper = Primitives.primitiveWrapperOf(booleanClass);
 assertFalse(booleanWrapper);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Class<Boolean> booleanClass = Boolean.class;
 Boolean booleanValue = Primitives.primitiveValueOrNullFor(booleanClass);
 assertFalse(booleanValue);
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 Class<Character> charClass = Character.class;
 Character charValue = Primitives.primitiveValueOrNullFor(charClass);
 assertEquals('\u0000', charValue);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Class<Byte> byteClass = Byte.class;
 Byte byteValue = Primitives.primitiveValueOrNullFor(byteClass);
 assertEquals((byte) 0, byteValue);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Class<Short> shortClass = Short.class;
 Short shortValue = Primitives.primitiveValueOrNullFor(shortClass);
 assertEquals((short) 0, shortValue);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Class<Integer> intClass = Integer.class;
 Integer intValue = Primitives.primitiveValueOrNullFor(intClass);
 assertEquals(0, intValue);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Class<Long> longClass = Long.class;
 Long longValue = Primitives.primitiveValueOrNullFor(longClass);
 assertEquals(0L, longValue);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Class<Float> floatClass = Float.class;
 Float floatValue = Primitives.primitiveValueOrNullFor(floatClass);
 assertEquals(0F, floatValue);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Class<Double> doubleClass = Double.class;
 Double doubleValue = Primitives.primitiveValueOrNullFor(doubleClass);
 assertEquals(0D, doubleValue);
 }
}