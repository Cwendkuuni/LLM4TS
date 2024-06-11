package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> expectedPrimitiveTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.TYPE);
 put(Character.class, Character.TYPE);
 put(Byte.class, Byte.TYPE);
 put(Short.class, Short.TYPE);
 put(Integer.class, Integer.TYPE);
 put(Long.class, Long.TYPE);
 put(Float.class, Float.TYPE);
 put(Double.class, Double.TYPE);
 }};

 private static final Map<Class<?>, Class<?>> expectedWrapperTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.class);
 put(Character.class, Character.class);
 put(Byte.class, Byte.class);
 put(Short.class, Short.class);
 put(Integer.class, Integer.class);
 put(Long.class, Long.class);
 put(Float.class, Float.class);
 put(Double.class, Double.class);
 }};

 private static final Map<Class<?>, Object> expectedWrapperReturnValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, false);
 put(Character.class, '\u0000');
 put(Byte.class, (byte) 0);
 put(Short.class, (short) 0);
 put(Integer.class, 0);
 put(Long.class, 0L);
 put(Float.class, 0F);
 put(Double.class, 0D);
 }};

 private static final Map<Class<?>, Object> expectedPrimitiveValues = new HashMap<Class<?>, Object>() {{
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
 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expected = entry.getValue();
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 boolean expected = true;
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 assertEquals(expected, actual);
 }

 for (Class<?> clazz : new Class[] { int.class, long.class, float.class, double.class }) {
 boolean expected = false;
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Class<?> wrapperType = entry.getKey();
 Class<?> primitiveType = entry.getValue();
 Object actual = Primitives.primitiveWrapperOf(wrapperType);
 assertEquals(primitiveType.getName(), actual.getClass().getName());
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 boolean expected = false;
 boolean actual = (boolean) Primitives.primitiveValueOrNullFor(boolean.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForCharacter() {
 char expected = '\u0000';
 char actual = (char) Primitives.primitiveValueOrNullFor(char.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForByte() {
 byte expected = 0;
 byte actual = (byte) Primitives.primitiveValueOrNullFor(byte.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForShort() {
 short expected = 0;
 short actual = (short) Primitives.primitiveValueOrNullFor(short.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForInteger() {
 int expected = 0;
 int actual = (int) Primitives.primitiveValueOrNullFor(int.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForLong() {
 long expected = 0L;
 long actual = (long) Primitives.primitiveValueOrNullFor(long.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testPrimitiveValueOrNullForFloat() {
 float expected = 0F;
 float actual = (float) Primitives.primitiveValueOrNullFor(float.class);
 assertEquals(expected, actual, 0.001f);
 }

 @Test
 public void testPrimitiveValueOrNullForDouble() {
 double expected = 0.0;
 double actual = (double) Primitives.primitiveValueOrNullFor(double.class);
 assertEquals(expected, actual, 0.000001);
 }
}