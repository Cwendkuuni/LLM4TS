package org.mockito.internal.util;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

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

 private static final Map<Class<?>, Object> expectedWrapperValues = new HashMap<Class<?>, Object>() {{
 put(Boolean.class, false);
 put(Character.class, (char) 0);
 put(Byte.class, (byte) 0);
 put(Short.class, (short) 0);
 put(Integer.class, 0);
 put(Long.class, 0L);
 put(Float.class, 0F);
 put(Double.class, 0D);
 }};

 @Test
 public void testPrimitiveTypeOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedPrimitiveTypes.entrySet()) {
 Class<?> clazz = entry.getKey();
 Class<?> expectedType = entry.getValue();
 Class<?> actualType = Primitives.primitiveTypeOf(clazz);
 Assert.assertEquals(expectedType, actualType);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Class<?> wrapperType = entry.getKey();
 boolean expectedResult = true;
 boolean actualResult = Primitives.isPrimitiveWrapper(wrapperType);
 Assert.assertEquals(expectedResult, actualResult);
 }

 for (Class<?> primitiveType : new Class[] { boolean.class, byte.class, short.class, int.class, long.class,
 char.class, float.class, double.class }) {
 boolean expectedResult = false;
 boolean actualResult = Primitives.isPrimitiveWrapper(primitiveType);
 Assert.assertEquals(expectedResult, actualResult);
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Map.Entry<Class<?>, Class<?>> entry : expectedWrapperTypes.entrySet()) {
 Class<?> wrapperType = entry.getKey();
 Object expectedValue = expectedWrapperValues.get(wrapperType);
 Object actualValue = Primitives.primitiveWrapperOf(wrapperType);
 Assert.assertEquals(expectedValue, actualValue);
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Map.Entry<Class<?>, Object> entry : expectedWrapperValues.entrySet()) {
 Class<?> primitiveType = entry.getKey();
 Object expectedValue = entry.getValue();
 Object actualValue = Primitives.primitiveValueOrNullFor(primitiveType);
 Assert.assertEquals(expectedValue, actualValue);
 }
 }
}