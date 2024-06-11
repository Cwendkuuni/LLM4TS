package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> wrapperTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.TYPE);
 put(Character.class, Character.TYPE);
 put(Byte.class, Byte.TYPE);
 put(Short.class, Short.TYPE);
 put(Integer.class, Integer.TYPE);
 put(Long.class, Long.TYPE);
 put(Float.class, Float.TYPE);
 put(Double.class, Double.TYPE);
 }};

 private static final Map<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.TYPE, Boolean.class);
 put(Character.TYPE, Character.class);
 put(Byte.TYPE, Byte.class);
 put(Short.TYPE, Short.class);
 put(Integer.TYPE, Integer.class);
 put(Long.TYPE, Long.class);
 put(Float.TYPE, Float.class);
 put(Double.TYPE, Double.class);
 }};

 private static final Map<Class<?>, Object> wrapperReturnValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, false);
 put(Character.class, '\u0000');
 put(Byte.class, (byte) 0);
 put(Short.class, (short) 0);
 put(Integer.class, 0);
 put(Long.class, 0L);
 put(Float.class, 0F);
 put(Double.class, 0D);
 }};

 private static final Map<Class<?>, Object> primitiveValues = new HashMap<Class<?>, Object>() {{
 put(boolean.class, false);
 put(char.class, '\u0000');
 put(byte.class, (byte) 0);
 put(short.class, (short) 0);
 put(int.class, 0);
 put(long.class, 0L);
 put(float.class, 0F);
 put(double.class, 0D);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : new Class[] { Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Byte[].class, String.class, List.class }) {
 Class<?> result = Primitives.primitiveTypeOf(clazz);
 assertEquals(result, Primitives.primitiveTypes.get(clazz));
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> type : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 assertTrue(Primitives.isPrimitiveWrapper(type));
 }
 for (Class<?> type : new Class[] { String.class, List.class, Boolean[].class }) {
 assertFalse(Primitives.isPrimitiveWrapper(type));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> type : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 assertEquals(Primitives.primitiveWrapperOf(type), Primitives.wrapperReturnValues.get(type));
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertEquals(Primitives.primitiveValueOrNullFor(boolean.class), false);
 }

 @Test
 public void testPrimitiveValueOrNullForChar() {
 assertEquals(Primitives.primitiveValueOrNullFor(char.class), '\u0000');
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 assertEquals(Primitives.primitiveValueOrNullFor(byte.class), (byte) 0);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 assertEquals(Primitives.primitiveValueOrNullFor(short.class), (short) 0);
 }

 @Test
 public void testPrimitiveValueOrNullForInt() {
 assertEquals(Primitives.primitiveValueOrNullFor(int.class), 0);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 assertEquals(Primitives.primitiveValueOrNullFor(long.class), 0L);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 assertEquals(Primitives.primitiveValueOrNullFor(float.class), 0F);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 assertEquals(Primitives.primitiveValueOrNullFor(double.class), 0D);
 }

 @Test
 public void testPrimitiveValueOrNullForArrayOfByte() {
 assertNull(Primitives.primitiveValueOrNullFor(byte[].class));
 }

 @Test
 public void testPrimitiveValueOrNullForString() {
 assertNull(Primitives.primitiveValueOrNullFor(String.class));
 }

 @Test
 public void testPrimitiveValueOrNullForList() {
 assertNull(Primitives.primitiveValueOrNullFor(List.class));
 }
}