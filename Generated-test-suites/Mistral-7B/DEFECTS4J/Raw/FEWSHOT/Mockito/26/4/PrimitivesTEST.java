package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimitivesTest {

 @Test
 public void testPrimitiveTypeOf() {
 Class<?> type = String.class;
 Class<?> result = Primitives.primitiveTypeOf(type);
 assertNull(result);

 type = Boolean.TYPE;
 result = Primitives.primitiveTypeOf(type);
 assertEquals(type, result);

 type = Character.TYPE;
 result = Primitives.primitiveTypeOf(type);
 assertEquals(type, result);

 type = Integer.class;
 result = Primitives.primitiveTypeOf(type);
 Class<Integer> intType = Integer.TYPE;
 assertEquals(intType, result);
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Class<?> type = Boolean.class;
 assertTrue(Primitives.isPrimitiveWrapper(type));

 type = Integer.class;
 assertFalse(Primitives.isPrimitiveWrapper(type));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Class<?> type = Boolean.class;
 Object result = Primitives.primitiveWrapperOf(type);
 assertTrue(result instanceof Boolean);
 assertEquals(false, ((Boolean) result).booleanValue());

 type = Character.class;
 result = Primitives.primitiveWrapperOf(type);
 assertTrue(result instanceof Character);
 assertEquals('\u0000', ((Character) result).charValue());
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 Object result = Primitives.primitiveValueOrNullFor(boolean.class);
 assertTrue(result instanceof Boolean);
 assertEquals(false, ((Boolean) result).booleanValue());
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 Object result = Primitives.primitiveValueOrNullFor(char.class);
 assertTrue(result instanceof Character);
 assertEquals('\u0000', ((Character) result).charValue());
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 Object result = Primitives.primitiveValueOrNullFor(byte.class);
 assertTrue(result instanceof Byte);
 assertEquals((byte) 0, ((Byte) result).byteValue());
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 Object result = Primitives.primitiveValueOrNullFor(short.class);
 assertTrue(result instanceof Short);
 assertEquals((short) 0, ((Short) result).shortValue());
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 Object result = Primitives.primitiveValueOrNullFor(int.class);
 assertTrue(result instanceof Integer);
 assertEquals(0, ((Integer) result).intValue());
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 Object result = Primitives.primitiveValueOrNullFor(long.class);
 assertTrue(result instanceof Long);
 assertEquals(0L, ((Long) result).longValue());
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 Object result = Primitives.primitiveValueOrNullFor(float.class);
 assertTrue(result instanceof Float);
 assertEquals(0F, ((Float) result).floatValue());
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 Object result = Primitives.primitiveValueOrNullFor(double.class);
 assertTrue(result instanceof Double);
 assertEquals(0D, ((Double) result).doubleValue());
 }
}