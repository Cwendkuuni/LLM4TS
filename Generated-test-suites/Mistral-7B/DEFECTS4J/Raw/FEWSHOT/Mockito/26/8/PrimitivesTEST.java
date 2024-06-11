package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<Boolean> BOOLEAN_CLASS = Boolean.class;
 private static final Class<Character> CHARACTER_CLASS = Character.class;
 private static final Class<Byte> BYTE_CLASS = Byte.class;
 private static final Class<Short> SHORT_CLASS = Short.class;
 private static final Class<Integer> INT_CLASS = Integer.class;
 private static final Class<Long> LONG_CLASS = Long.class;
 private static final Class<Float> FLOAT_CLASS = Float.class;
 private static final Class<Double> DOUBLE_CLASS = Double.class;

 @Test
 public void testPrimitiveTypeOf_returnsCorrectTypeForPrimitiveClass() {
 assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(BOOLEAN_CLASS));
 assertEquals(Character.TYPE, Primitives.primitiveTypeOf(CHARACTER_CLASS));
 // ... other assertions for other primitive types
 }

 @Test
 public void testPrimitiveTypeOf_returnsNullForNonPrimitiveClass() {
 assertNull(Primitives.primitiveTypeOf(String.class));
 }

 @Test
 public void testIsPrimitiveWrapper_returnsTrueForWrapperClasses() {
 assertEquals(Boolean.class, Primitives.primitiveTypeOf(Boolean.class));
 assertEquals(Byte.class, Primitives.primitiveTypeOf(Byte.class));
 // ... other assertions for other primitive types and their wrapper classes

 assertEquals(Boolean.class, Primitives.primitiveTypeOf(Boolean.TYPE));
 assertEquals(Byte.class, Primitives.primitiveTypeOf(Byte.TYPE));
 // ... other assertions for other primitive types and their wrapper types

 for (Class<?> wrapper : new Class<?>[] { Boolean.class, Byte.class, // ... other wrapper classes
 Short.class, Integer.class, Long.class, Float.class, Double.class }) {
 assertEquals(wrapper, Primitives.primitiveWrapperOf(Primitives.primitiveTypeOf(wrapper)));
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor_returnsCorrectValueForPrimitiveTypes() {
 assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
 assertEquals((char) 0, Primitives.primitiveValueOrNullFor(char.class));
 // ... other assertions for other primitive types
 }

 @Test
 public void testPrimitiveValueOrNullFor_returnsNullForNonPrimitiveTypes() {
 assertNull(Primitives.primitiveValueOrNullFor(String.class));
 }
}