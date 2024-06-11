package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private Class<?>[] primitiveTypes = {Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class};
 private Object[] primitiveValues = {false, '\u0000', (byte) 0, (short) 0, 0, 0L, 0F, 0D};

 @Test
 public void testPrimitiveTypeOf() {
 for (int i = 0; i < primitiveTypes.length; i++) {
 Class<?> actual = Primitives.primitiveTypeOf(primitiveTypes[i]);
 Class<?> expected = primitiveTypes[i];
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> type : primitiveTypes) {
 boolean actual = Primitives.isPrimitiveWrapper(type);
 boolean expected = false;
 if (type.isPrimitive()) {
 expected = true;
 }
 assertFalse(actual); // False for primitives

 Class<?> wrapperType = Primitives.primitiveTypeOf(TypeHelper.wrap(type));
 actual = Primitives.isPrimitiveWrapper(wrapperType);
 assertTrue(actual); // True for wrappers
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (int i = 0; i < primitiveTypes.length; i++) {
 Object actual = Primitives.primitiveWrapperOf(primitiveTypes[i]);
 Object expected = primitiveValues[i];
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (int i = 0; i < primitiveTypes.length; i++) {
 Object actual = Primitives.primitiveValueOrNullFor(primitiveTypes[i]);
 Object expected = primitiveValues[i];
 assertEquals(expected, actual);
 }
 }
}