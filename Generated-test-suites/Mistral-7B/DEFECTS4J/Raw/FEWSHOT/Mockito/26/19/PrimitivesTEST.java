package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> primitiveTypesMap = new HashMap<Class<?>, Class<?>>() {{
 putAll(Primitives.primitiveTypes);
 }};
 private static final Map<Class<?>, Object> wrapperReturnValuesMap = new HashMap<Class<?>, Object>() {{
 putAll(Primitives.wrapperReturnValues);
 }};
 private static final Map<Class<?>, Object> primitiveValuesMap = new HashMap<Class<?>, Object>() {{
 putAll(Primitives.primitiveValues);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 FloatingPointType.class, Double.class }) {
 Class<?> result = Primitives.primitiveTypeOf(clazz);
 assertSame(clazz.getPrimitiveType(), result);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> clazz : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 FloatingPointType.class, Double.class }) {
 assertSame(Boolean.class, Primitives.primitiveTypeOf(clazz));
 assertSame(clazz, Primitives.wrapperTypeOf(Primitives.primitiveTypeOf(clazz)));
 assertTrue(Primitives.isPrimitiveWrapper(clazz));
 }
 }

 @Test
 public void testWrapperTypeOf() {
 for (Class<?> clazz : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 FloatingPointType.class, Double.class }) {
 Class<?> result = Primitives.wrapperTypeOf(Primitives.primitiveTypeOf(clazz));
 assertSame(clazz, result);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> wrapperType : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 FloatingPointType.class, Double.class }) {
 Object result = Primitives.primitiveWrapperOf(wrapperType);
 assertSame(wrapperReturnValuesMap.get(wrapperType), result);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertTrue(Primitives.primitiveValueOrNullFor(boolean.class) instanceof Boolean);
 assertSame(false, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 assertTrue(Primitives.primitiveValueOrNullFor(char.class) instanceof Character);
 assertSame('\u0000', Primitives.primitiveValueOrNullFor(char.class));
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 assertTrue(Primitives.primitiveValueOrNullFor(byte.class) instanceof Byte);
 assertSame((byte) 0, Primitives.primitiveValueOrNullFor(byte.class));
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 assertTrue(Primitives.primitiveValueOrNullFor(short.class) instanceof Short);
 assertSame((short) 0, Primitives.primitiveValueOrNullFor(short.class));
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 assertTrue(Primitives.primitiveValueOrNullFor(int.class) instanceof Integer);
 assertSame(0, Primitives.primitiveValueOrNullFor(int.class));
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 assertTrue(Primitives.primitiveValueOrNullFor(long.class) instanceof Long);
 assertSame(0L, Primitives.primitiveValueOrNullFor(long.class));
 }

 @Test
 public void testPrimitiveValueOrNullForFloatingPointType() {
 float result = (float) Primitives.primitiveValueOrNullFor(float.class);
 assertTrue(result == 0F || result == Float.NaN);

 double result2 = (double) Primitives.primitiveValueOrNullFor(double.class);
 assertTrue(result2 == 0 || result2 == Double.NaN);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 assertTrue(Primitives.primitiveValueOrNullFor(double.class) instanceof Double);
 assertSame(0D, Primitives.primitiveValueOrNullFor(double.class));
 }

 @Test
 public void testIsNullForNull() {
 assertNull(Primitives.primitiveValueOrNullFor(null));
 }
}

 class FloatingPointType {
 float f;
 double d;
 }