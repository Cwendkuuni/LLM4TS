package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_WRAPPER_TYPES = {
     Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
     Long.class, Float.class, Double.class
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (Class<?> clazz : new Class<?>[] { int.class, Integer.class, int[].class, Integer[].class }) {
         assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(clazz));
     }

     for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
         assertEquals(clazz, Primitives.primitiveTypeOf(clazz));
     }

     for (Class<?> clazz : new Class<?>[] { Boolean.class, String.class }) {
         assertNull(Primitives.primitiveTypeOf(clazz));
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (Class<?> clazz : PRIMITIVE_WRAPPER_TYPES) {
         assertTrue(Primitives.isPrimitiveWrapper(clazz));
     }

     assertFalse(Primitives.isPrimitiveWrapper(Integer.class));
     assertFalse(Primitives.isPrimitiveWrapper(String.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (Class<?> wrapperType : PRIMITIVE_WRAPPER_TYPES) {
         for (Class<?> primitiveType : new Class<?>[] {
                 Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE,
                 Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE }) {

             @SuppressWarnings("unchecked")
             T actual = (T) Primitives.primitiveWrapperOf(wrapperType);
             @SuppressWarnings("unchecked")
             Class<T> expected = (Class<T>) primitiveType;
             assertEquals(expected, wrapperType);
             assertEquals(expected.newInstance(), actual);
         }
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (Class<?> primitiveType : new Class<?>[] {
             boolean.class, char.class, byte.class, short.class,
             int.class, long.class, float.class, double.class }) {
         assertEquals(primitiveType.getName(), Primitives.primitiveValueOrNullFor(primitiveType).getClass().getName());
         assertEquals(primitiveType.cast(null), Primitives.primitiveValueOrNullFor(primitiveType));
         assertNotNull(Primitives.primitiveValueOrNullFor(primitiveType));
     }
 }

 @Test
 public void testWrapperTypes() {
     for (Class<?> wrapperType : PRIMITIVE_WRAPPER_TYPES) {
         @SuppressWarnings("unchecked")
         Class<T> expected = (Class<T>) Primitives.primitiveTypeOf(wrapperType);
         assertSame(expected, Primitives.wrapperTypes.get(wrapperType));
     }
 }

 @Test
 public void testPrimitiveTypes() {
     for (Class<?> primitiveType : new Class<?>[] {
             Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE,
             Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE }) {
         @SuppressWarnings("unchecked")
         Class<T> expected = (Class<T>) Primitives.primitiveTypeOf(primitiveType);
         assertSame(expected, Primitives.primitiveTypes.get(primitiveType.getName()));
     }
 }

 @Test
 public void testWrapperReturnValues() {
     for (Class<?> wrapperType : PRIMITIVE_WRAPPER_TYPES) {
         assertEquals(Primitives.wrapperReturnValues.get(wrapperType), Primitives.primitiveValueOrNullFor(wrapperType.getName()));
     }
 }

 @Test
 public void testPrimitiveValues() {
     for (Class<?> primitiveType : new Class<?>[] {
             boolean.class, char.class, byte.class, short.class,
             int.class, long.class, float.class, double.class }) {
         assertEquals(Primitives.primitiveValues.get(primitiveType), Primitives.wrapperReturnValues.get(primitiveType.getName()));
     }
 }
}