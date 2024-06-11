package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrimitivesTest {

 private static final Class<?>[] BOOLEAN_WRAPPER_TYPES = { Boolean.class, Boolean.TYPE };
 private static final Boolean BOOLEAN_PRIMITIVE_VALUE = false;

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> type : BOOLEAN_WRAPPER_TYPES) {
 assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(type));
 }

 for (Class<?> type : new Class[] { String.class, List.class }) {
 assertNull(Primitives.primitiveTypeOf(type));
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> type : BOOLEAN_WRAPPER_TYPES) {
 assertEquals(true, Primitives.isPrimitiveWrapper(type));
 }

 for (Class<?> type : new Class[] { int.class, Integer.class }) {
 assertEquals(false, Primitives.isPrimitiveWrapper(type));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> type : BOOLEAN_WRAPPER_TYPES) {
 assertEquals(BOOLEAN_PRIMITIVE_VALUE, Primitives.primitiveWrapperOf(type));
 }
 }

 @Test
 public void testPrimitiveValueOrNullForBoolean() {
 assertEquals(BOOLEAN_PRIMITIVE_VALUE, Primitives.primitiveValueOrNullFor(boolean.class));
 }

 @Test
 public void testPrimitiveValueOrNullForNull() {
 assertNull(Primitives.primitiveValueOrNullFor(null));
 }
}