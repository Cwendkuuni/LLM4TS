package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = new Class<?>[] {
 Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
 Float.TYPE, Double.TYPE
 };

 @Test
 public void testPrimitiveTypeOf() {
 for (Class<?> clazz : PRIMITIVE_TYPES) {
 Class<?> actual = Primitives.primitiveTypeOf(clazz);
 Class<?> expected = clazz;
 Assert.assertEquals("primitiveTypeOf(Boolean.TYPE)", expected, actual);
 }

 for (Class<?> wrapperType : PRIMITIVE_TYPES) {
 Class<?> actual = Primitives.primitiveTypeOf(WrapperType.classOf(wrapperType));
 Class<?> expected = wrapperType;
 Assert.assertEquals("primitiveTypeOf(Integer.class)", expected, actual);
 }
 }

 @Test
 public void testIsPrimitiveWrapper() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Assert.assertFalse("isPrimitiveWrapper(Boolean.TYPE)", Primitives.isPrimitiveWrapper(primitiveType));
 }

 for (Class<?> wrapperType : PRIMITIVE_TYPES) {
 Class<?> primitiveType = Primitives.primitiveTypeOf(wrapperType);
 Assert.assertTrue("isPrimitiveWrapper(Integer.class)", Primitives.isPrimitiveWrapper(WrapperType.classOf(wrapperType)));
 Assert.assertFalse("isPrimitiveWrapper(Integer.class)", Primitives.isPrimitiveWrapper(primitiveType));
 }
 }

 @Test
 public void testPrimitiveWrapperOf() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object actual = Primitives.primitiveWrapperOf(primitiveType);
 Class<?> expectedClass = primitiveType;
 if (expectedClass.isPrimitive()) {
 Assert.assertEquals("primitiveWrapperOf(boolean.class)", false, actual);
 continue;
 }
 Object expected = Primitives.wrapperReturnValues.get(primitiveType);
 Assert.assertEquals("primitiveWrapperOf(Integer.class)", expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrimitiveWrapperOfNull() {
 Primitives.primitiveWrapperOf(null);
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 for (Class<?> primitiveType : PRIMITIVE_TYPES) {
 Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
 Object expected = Primitives.primitiveValues.get(primitiveType);
 Assert.assertEquals("primitiveValueOrNullFor(boolean.class)", expected, actual);
 }
 }

 @Test
 public void testPrimitiveValueOrNullForNull() {
 Assert.assertNull("primitiveValueOrNullFor(null)", Primitives.primitiveValueOrNullFor(null));
 }

 private static class WrapperType<T> {
 public static <T> Class<T> classOf(Class<T> clazz) {
 if (clazz.isPrimitive()) {
 return (Class<T>) Primitives.primitiveTypes.get(clazz);
 }
 return (Class<T>) clazz;
 }
 }
}