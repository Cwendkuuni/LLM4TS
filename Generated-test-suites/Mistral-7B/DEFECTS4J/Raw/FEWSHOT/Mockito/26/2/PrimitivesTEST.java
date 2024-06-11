package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {
Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
Float.class, Double.class};

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 Class<?> expected = Primitives.primitiveTypeOf(clazz);
 assertEquals(expected, clazz.getComponentType());
 }

 Class<?> stringClass = String.class;
 Class<?> expected = null;
 assertNull(Primitives.primitiveTypeOf(stringClass));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 assertTrue(Primitives.isPrimitiveWrapper(clazz));
 }

 Class<?> stringClass = String.class;
 assertFalse(Primitives.isPrimitiveWrapper(stringClass));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 assertEquals(Primitives.primitiveWrapperOf(clazz).booleanValue(), false);
 }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
 assertNotNull(Primitives.primitiveValueOrNullFor(clazz));
 assertEquals(Primitives.primitiveValueOrNullFor(clazz), Primitives.wrapperReturnValues.get(clazz));
 }

 Class<?> booleanClass = boolean.class;
 assertNull(Primitives.primitiveValueOrNullFor(booleanClass));
 }
}