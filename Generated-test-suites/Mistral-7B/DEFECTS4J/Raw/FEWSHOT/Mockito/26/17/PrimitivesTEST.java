package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<Boolean> booleanClass = Boolean.class;
 Class<Boolean> expectedType = Boolean.TYPE;
 Class<Boolean> result = Primitives.primitiveTypeOf(booleanClass);
 assertEquals(expectedType, result);

 Class<Character> characterClass = Character.class;
 Class<Character> expectedCharType = Character.TYPE;
 Class<Character> resultChar = Primitives.primitiveTypeOf(characterClass);
 assertEquals(expectedCharType, resultChar);

 Class<Integer> integerClass = Integer.class;
 Class<Integer> expectedIntType = Integer.TYPE;
 Class<Integer> resultInt = Primitives.primitiveTypeOf(integerClass);
 assertEquals(expectedIntType, resultInt);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Boolean> booleanWrapperClass = Boolean.class;
 assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));

 Class<Character> characterWrapperClass = Character.class;
 assertTrue(Primitives.isPrimitiveWrapper(characterWrapperClass));

 Class<String> stringClass = String.class;
 assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanWrapperClass = Boolean.class;
 Boolean expectedBoolean = false;
 Boolean resultBoolean = Primitives.primitiveWrapperOf(booleanWrapperClass);
 assertEquals(expectedBoolean, resultBoolean);

 Class<Character> characterWrapperClass = Character.class;
 Character expectedCharacter = '\u0000';
 Character resultCharacter = Primitives.primitiveWrapperOf(characterWrapperClass);
 assertEquals(expectedCharacter, resultCharacter);
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Class<Boolean> booleanClass = Boolean.class;
 Boolean expectedBoolean = false;
 Boolean resultBoolean = Primitives.primitiveValueOrNullFor(booleanClass);
 assertEquals(expectedBoolean, resultBoolean);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 Class<Character> characterClass = Character.class;
 Character expectedCharacter = '\u0000';
 Character resultCharacter = Primitives.primitiveValueOrNullFor(characterClass);
 assertEquals(expectedCharacter, resultCharacter);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Class<Byte> byteClass = Byte.class;
 Byte expectedByte = (byte) 0;
 Byte resultByte = Primitives.primitiveValueOrNullFor(byteClass);
 assertEquals(expectedByte, resultByte);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Class<Short> shortClass = Short.class;
 Short expectedShort = (short) 0;
 Short resultShort = Primitives.primitiveValueOrNullFor(shortClass);
 assertEquals(expectedShort, resultShort);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Class<Integer> intClass = Integer.class;
 Integer expectedInt = 0;
 Integer resultInt = Primitives.primitiveValueOrNullFor(intClass);
 assertEquals(expectedInt, resultInt);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Class<Long> longClass = Long.class;
 Long expectedLong = 0L;
 Long resultLong = Primitives.primitiveValueOrNullFor(longClass);
 assertEquals(expectedLong, resultLong);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Class<Float> floatClass = Float.class;
 Float expectedFloat = 0F;
 Float resultFloat = Primitives.primitiveValueOrNullFor(floatClass);
 assertEquals(expectedFloat, resultFloat);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Class<Double> doubleClass = Double.class;
 Double expectedDouble = 0D;
 Double resultDouble = Primitives.primitiveValueOrNullFor(doubleClass);
 assertEquals(expectedDouble, resultDouble);
 }
}