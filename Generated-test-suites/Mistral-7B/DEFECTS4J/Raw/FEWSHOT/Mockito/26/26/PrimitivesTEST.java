package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PrimitivesTest {

 private static final Map<Class<?>, Class<?>> expectedWrapperTypes = new HashMap<Class<?>, Class<?>>() {{
 put(Boolean.class, Boolean.TYPE);
 put(Character.class, Character.TYPE);
 put(Byte.class, Byte.TYPE);
 put(Short.class, Short.TYPE);
 put(Integer.class, Integer.TYPE);
 put(Long.class, Long.TYPE);
 put(Float.class, Float.TYPE);
 put(Double.class, Double.TYPE);
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
 public void testPrimitives() {
 // test primitiveTypeOf
 for (Class<?> clazz : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 Float.class, Double.class }) {
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 assertEquals(expectedWrapperTypes.get(clazz), actual);
 }

 // test isPrimitiveWrapper
 for (Class<?> clazz : new Class[] { Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
 Float.class, Double.class }) {
 boolean actual = Primitives.isPrimitiveWrapper(clazz);
 assertFalse("Expected false for non-wrapper type: " + clazz, actual);
 }
 for (Class<?> wrapperClazz : new Class[] { Boolean.TYPE.getClass(), Character.TYPE.getClass(), Byte.TYPE.getClass(),
 Short.TYPE.getClass(), Integer.TYPE.getClass(), Long.TYPE.getClass(), Float.TYPE.getClass(), Double.TYPE.getClass() }) {
 boolean actual = Primitives.isPrimitiveWrapper(wrapperClazz);
 assertTrue("Expected true for wrapper type: " + wrapperClazz, actual);
 }

 // test primitiveWrapperOf
 for (Class<?> wrapperClazz : new Class[] { Boolean.TYPE.getClass(), Character.TYPE.getClass(), Byte.TYPE.getClass(),
 Short.TYPE.getClass(), Integer.TYPE.getClass(), Long.TYPE.getClass(), Float.TYPE.getClass(), Double.TYPE.getClass() }) {
 Object actual = Primitives.primitiveWrapperOf(wrapperClazz);
 assertEquals(expectedWrapperReturnValues.get(wrapperClazz), actual);
 }

 // test primitiveValueOrNullFor
 for (Class<?> primitiveClazz : new Class[] { boolean.class, char.class, byte.class, short.class, int.class, long.class,
 Float.class, Double.class }) {
 Object actual = Primitives.primitiveValueOrNullFor(primitiveClazz);
 assertEquals(expectedPrimitiveValues.get(primitiveClazz), actual);
 }
 }

 @Test
 public void testWrapperTypes() {
 Map<Class<?>, Class<?>> actual = Primitives.wrapperTypes;
 assertEquals(expectedWrapperTypes, actual);
 }

 @Test
 public void testPrimitiveTypes() {
 Map<Class<?>, Class<?>> actual = Primitives.primitiveTypes;
 assertEquals(expectedWrapperTypes, actual); // Note: This is a side effect of how we initialized expectedWrapperTypes
 }

 @Test
 public void testWrapperReturnValues() {
 Map<Class<?>, Object> actual = Primitives.wrapperReturnValues;
 assertEquals(expectedWrapperReturnValues, actual);
 }

 @Test
 public void testPrimitiveValues() {
 Map<Class<?>, Object> actual = Primitives.primitiveValues;
 assertEquals(expectedPrimitiveValues, actual);
 }
}