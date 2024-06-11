package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

 private static final Map<Class<?>, Object> expectedWrapperValues = new HashMap<Class<?>, Object>() {{
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
 Class<?> actual = Primitives.primitiveTypeOf(entry.getKey());
 assertEquals(entry.getValue(), actual);
 }

 for (Map.Entry<Class<?>, Class<?>> entry : wrapperTypes.entrySet()) {
 Class<?> actual = Primitives.primitiveTypeOf(entry.getKey());
 assertNull(actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : wrapperTypes.entrySet()) {
 assertTrue(Primitives.isPrimitiveWrapper(entry.getKey()));
 }

 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 assertFalse(Primitives.isPrimitiveWrapper(entry.getKey()));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 assertEquals(entry.getValue(), Primitives.primitiveWrapperOf(entry.getKey()));
 }

 for (Map.Entry<Class<?>, Class<?>> entry : wrapperTypes.entrySet()) {
 assertNotNull(Primitives.primitiveWrapperOf(entry.getKey()));
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : expectedPrimitiveValues.entrySet()) {
 assertEquals(entry.getValue(), Primitives.primitiveValueOrNullFor(entry.getKey()));
 }

 for (Class<?> unknownType : new Class<?>[] { String.class, List.class }) {
 assertNull(Primitives.primitiveValueOrNullFor(unknownType));
 }
 }

 @Test
 public void testWrapperTypes() {
 assertNotNull(Primitives.wrapperTypes);
 assertEquals(expectedWrapperTypes.size(), Primitives.wrapperTypes.size());
 }

 @Test
 public void testPrimitiveTypes() {
 assertNotNull(Primitives.primitiveTypes);
 assertEquals(expectedPrimitiveTypes.size(), Primitives.primitiveTypes.size());
 }

 @Test
 public void testWrapperValues() {
 assertNotNull(Primitives.wrapperReturnValues);
 assertEquals(expectedWrapperValues.size(), Primitives.wrapperReturnValues.size());
 }

 @Test
 public void testPrimitiveValues() {
 assertNotNull(Primitives.primitiveValues);
 assertEquals(expectedPrimitiveValues.size(), Primitives.primitiveValues.size());
 }
}