package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<Byte> byteType = Byte.TYPE;
 Class<Boolean> booleanType = Boolean.TYPE;
 Class<Character> charType = Character.TYPE;
 Class<Integer> intType = Integer.TYPE;
 Class<Double> doubleType = Double.TYPE;
 Class<String> stringType = String.class;

 assertEquals(byteType, Primitives.primitiveTypeOf(Byte.class));
 assertEquals(booleanType, Primitives.primitiveTypeOf(Boolean.class));
 assertEquals(charType, Primitives.primitiveTypeOf(Character.class));
 assertEquals(intType, Primitives.primitiveTypeOf(Integer.class));
 assertEquals(doubleType, Primitives.primitiveTypeOf(Double.class));
 assertNotEquals(stringType, Primitives.primitiveTypeOf(String.class));
 }

 @Test
 public void testPrimitiveTypesAreMappedCorrectly() {
 Map<Class<?>, Class<?>> primitiveTypesMap = Primitives.primitiveTypes;

 assertEquals(Boolean.TYPE, primitiveTypesMap.get(Boolean.class));
 assertEquals(Character.TYPE, primitiveTypesMap.get(Character.class));
 assertEquals(Byte.TYPE, primitiveTypesMap.get(Byte.class));
 assertEquals(Short.TYPE, primitiveTypesMap.get(Short.class));
 assertEquals(Integer.TYPE, primitiveTypesMap.get(Integer.class));
 assertEquals(Long.TYPE, primitiveTypesMap.get(Long.class));
 assertEquals(Float.TYPE, primitiveTypesMap.get(Float.class));
 assertEquals(Double.TYPE, primitiveTypesMap.get(Double.class));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<Boolean> booleanWrapperType = Boolean.class;
 Class<Byte> byteWrapperType = Byte.class;
 Class<Character> charWrapperType = Character.class;
 Class<Integer> intWrapperType = Integer.class;
 Class<Long> longWrapperType = Long.class;

 assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 assertTrue(Primitives.isPrimitiveWrapper(Character.class));
 assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
 assertTrue(Primitives.isPrimitiveWrapper(Long.class));

 assertFalse(Primitives.isPrimitiveWrapper(Boolean.class.getComponentType()));
 assertFalse(Primitives.isPrimitiveWrapper(Byte[].class));
 assertFalse(Primitives.isPrimitiveWrapper(Character[].class));
 assertFalse(Primitives.isPrimitiveWrapper(Integer[].class));
 assertFalse(Primitives.isPrimitiveWrapper(Long[].class));

 assertEquals(Boolean.class, Primitives.primitiveTypeOf(booleanWrapperType));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<Boolean> booleanType = Boolean.TYPE;
 Class<Boolean> booleanWrapperType = Boolean.class;
 Class<Byte> byteType = Byte.TYPE;
 Class<Byte> byteWrapperType = Byte.class;
 Class<Character> charType = Character.TYPE;
 Class<Character> charWrapperType = Character.class;
 Class<Integer> intType = Integer.TYPE;
 Class<Integer> intWrapperType = Integer.class;
 Class<Long> longType = Long.TYPE;
 Class<Long> longWrapperType = Long.class;
 Class<Float> floatType = Float.TYPE;
 Class<Float> floatWrapperType = Float.class;
 Class<Double> doubleType = Double.TYPE;
 Class<Double> doubleWrapperType = Double.class;

 assertNull(Primitives.primitiveWrapperOf(booleanType));
 assertNull(Primitives.primitiveWrapperOf(charType));
 assertNull(Primitives.primitiveWrapperOf(byteType));
 assertNull(Primitives.primitiveWrapperOf(shortType));
 assertNull(Primitives.primitiveWrapperOf(intType));
 assertNull(Primitives.primitiveWrapperOf(longType));
 assertNull(Primitives.primitiveWrapperOf(floatType));
 assertNull(Primitives.primitiveWrapperOf(doubleType));

 assertEquals(false, (Boolean) Primitives.primitiveWrapperOf(booleanWrapperType));
 assertEquals('\u0000', (Character) Primitives.primitiveWrapperOf(charWrapperType));
 assertEquals((byte) 0, (Byte) Primitives.primitiveWrapperOf(byteWrapperType));
 assertEquals((short) 0, (Short) Primitives.primitiveWrapperOf(intWrapperType));
 assertEquals(0, (Integer) Primitives.primitiveWrapperOf(intWrapperType));
 assertEquals(0L, (Long) Primitives.primitiveWrapperOf(longWrapperType));
 assertEquals(0F, (Float) Primitives.primitiveWrapperOf(floatWrapperType));
 assertEquals(0D, (Double) Primitives.primitiveWrapperOf(doubleWrapperType));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Class<Boolean> booleanType = Boolean.TYPE;
 Class<Boolean> booleanWrapperType = Boolean.class;
 Class<Byte> byteType = Byte.TYPE;
 Class<Byte> byteWrapperType = Byte.class;
 Class<Character> charType = Character.TYPE;
 Class<Character> charWrapperType = Character.class;
 Class<Integer> intType = Integer.TYPE;
 Class<Integer> intWrapperType = Integer.class;
 Class<Long> longType = Long.TYPE;
 Class<Long> longWrapperType = Long.class;
 Class<Float> floatType = Float.TYPE;
 Class<Float> floatWrapperType = Float.class;
 Class<Double> doubleType = Double.TYPE;
 Class<Double> doubleWrapperType = Double.class;

 assertEquals(false, Primitives.primitiveValueOrNullFor(booleanType));
 assertEquals('\u0000', (Character) Primitives.primitiveValueOrNullFor(charType));
 assertEquals((byte) 0, (Byte) Primitives.primitiveValueOrNullFor(byteType));
 assertEquals((short) 0, (Short) Primitives.primitiveValueOrNullFor(intType));
 assertEquals(0, (Integer) Primitives.primitiveValueOrNullFor(intType));
 assertEquals(0L, (Long) Primitives.primitiveValueOrNullFor(longType));
 assertEquals(0F, (Float) Primitives.primitiveValueOrNullFor(floatType));
 assertEquals(0D, (Double) Primitives.primitiveValueOrNullFor(doubleType));

 assertNull(Primitives.primitiveValueOrNullFor(booleanWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(byteWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(charWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(intWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(longWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(floatWrapperType));
 assertNull(Primitives.primitiveValueOrNullFor(doubleWrapperType));
 }
}