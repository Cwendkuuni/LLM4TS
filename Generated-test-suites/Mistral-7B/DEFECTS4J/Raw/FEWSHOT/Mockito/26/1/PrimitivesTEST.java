package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
     boolean.class, char.class, byte.class, short.class,
     int.class, long.class, float.class, double.class
 };

 private static final Class<?>[] WRAPPER_TYPES = {
     Boolean.class, Character.class, Byte.class, Short.class,
     Integer.class, Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         assertEquals(primitiveType, Primitives.primitiveTypeOf(primitiveType));
         assertEquals(primitiveType, Primitives.primitiveTypeOf(Primitives.primitiveTypeOf(primitiveType)));
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (Class<?> wrapperType : WRAPPER_TYPES) {
         assertTrue(Primitives.isPrimitiveWrapper(wrapperType));
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         assertSame(primitiveType.getComponentType(), Primitives.primitiveWrapperOf(primitiveType).getClass().getComponentType());
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         assertSame(primitiveValues.get(primitiveType), Primitives.primitiveValueOrNullFor(primitiveType));
     }

     assertNull(Primitives.primitiveValueOrNullFor(Object.class));
 }

 @Test
 public void testWrapperTypes() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         assertEquals(PRIMITIVE_TYPES[i], Primitives.primitiveTypeOf(WRAPPER_TYPES[i]));
     }
 }

 @Test
 public void testWrapperReturnValues() {
     for (Class<?> primitiveType : PRIMITIVE_TYPES) {
         assertEquals(Primitives.primitiveValues.get(primitiveType), Primitives.primitiveWrapperOf(primitiveType));
     }
 }
}