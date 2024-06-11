package org.mockito.internal.util;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class PrimitivesTest {

 private Primitives primitives = new Primitives();

 @Test
 public void testPrimitiveTypeOf() {
 Map<Class<?>, Class<?>> primitiveTypes = primitives.primitiveTypes;

 Assert.assertEquals(Boolean.TYPE, primitives.primitiveTypeOf(Boolean.class));
 Assert.assertEquals(Boolean.TYPE, primitives.primitiveTypeOf(boolean.class));
 Assert.assertEquals(Character.TYPE, primitives.primitiveTypeOf(Character.class));
 Assert.assertEquals(Character.TYPE, primitives.primitiveTypeOf(char.class));
 Assert.assertEquals(Byte.TYPE, primitives.primitiveTypeOf(Byte.class));
 Assert.assertEquals(Byte.TYPE, primitives.primitiveTypeOf(byte.class));
 Assert.assertEquals(Short.TYPE, primitives.primitiveTypeOf(Short.class));
 Assert.assertEquals(Short.TYPE, primitives.primitiveTypeOf(short.class));
 Assert.assertEquals(Integer.TYPE, primitives.primitiveTypeOf(Integer.class));
 Assert.assertEquals(Integer.TYPE, primitives.primitiveTypeOf(int.class));
 Assert.assertEquals(Long.TYPE, primitives.primitiveTypeOf(Long.class));
 Assert.assertEquals(Long.TYPE, primitives.primitiveTypeOf(long.class));
 Assert.assertEquals(Float.TYPE, primitives.primitiveTypeOf(Float.class));
 Assert.assertEquals(Float.TYPE, primitives.primitiveTypeOf(float.class));
 Assert.assertEquals(Double.TYPE, primitives.primitiveTypeOf(Double.class));
 Assert.assertEquals(Double.TYPE, primitives.primitiveTypeOf(double.class));

 Assert.assertEquals(null, primitives.primitiveTypeOf(String.class));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 Map<Class<?>, Class<?>> primitiveTypes = primitives.primitiveTypes;
 Map<Class<?>, Object> wrapperReturnValues = primitives.wrapperReturnValues;

 Assert.assertFalse(primitives.isPrimitiveWrapper(Integer.class));
 Assert.assertTrue(primitives.isPrimitiveWrapper(Integer.class.wrapperType()));
 Assert.assertFalse(primitives.isPrimitiveWrapper(Character.class));
 Assert.assertTrue(primitives.isPrimitiveWrapper(Character.class.wrapperType()));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 Map<Class<?>, Object> wrapperReturnValues = primitives.wrapperReturnValues;

 Assert.assertEquals(false, primitives.primitiveWrapperOf(Boolean.class));
 Assert.assertEquals(false, primitives.primitiveWrapperOf(boolean.class));
 Assert.assertEquals('\u0000', primitives.primitiveWrapperOf(Character.class));
 Assert.assertEquals('\u0000', primitives.primitiveWrapperOf(char.class));
 Assert.assertEquals((byte) 0, primitives.primitiveWrapperOf(Byte.class));
 Assert.assertEquals((byte) 0, primitives.primitiveWrapperOf(byte.class));
 Assert.assertEquals((short) 0, primitives.primitiveWrapperOf(Short.class));
 Assert.assertEquals((short) 0, primitives.primitiveWrapperOf(short.class));
 Assert.assertEquals(0, primitives.primitiveWrapperOf(Integer.class));
 Assert.assertEquals(0, primitives.primitiveWrapperOf(int.class));
 Assert.assertEquals(0L, primitives.primitiveWrapperOf(Long.class));
 Assert.assertEquals(0L, primitives.primitiveWrapperOf(long.class));
 Assert.assertEquals(0F, primitives.primitiveWrapperOf(Float.class));
 Assert.assertEquals(0F, primitives.primitiveWrapperOf(float.class));
 Assert.assertEquals(0D, primitives.primitiveWrapperOf(Double.class));
 Assert.assertEquals(0D, primitives.primitiveWrapperOf(double.class));

 Assert.assertNull(primitives.primitiveWrapperOf(String.class));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 Map<Class<?>, Object> primitiveValues = primitives.primitiveValues;

 Assert.assertEquals(false, primitives.primitiveValueOrNullFor(boolean.class));
 Assert.assertEquals(false, primitives.primitiveValueOrNullFor(Boolean.class));
 Assert.assertEquals('\u0000', (char) primitives.primitiveValueOrNullFor(char.class));
 Assert.assertEquals(0, primitives.primitiveValueOrNullFor(byte.class).byteValue());
 Assert.assertEquals(0, primitives.primitiveValueOrNullFor(short.class).shortValue());
 Assert.assertEquals(0, primitives.primitiveValueOrNullFor(int.class).intValue());
 Assert.assertEquals(0L, primitives.primitiveValueOrNullFor(long.class).longValue());
 Assert.assertEquals(0F, primitives.primitiveValueOrNullFor(float.class).floatValue());
 Assert.assertEquals(0D, primitives.primitiveValueOrNullFor(double.class).doubleValue());
 }
}