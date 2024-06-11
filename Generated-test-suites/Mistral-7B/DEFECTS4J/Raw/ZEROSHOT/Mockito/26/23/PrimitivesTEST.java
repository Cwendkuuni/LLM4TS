package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Class<?>[] PRIMITIVE_TYPES = {
         Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
         Float.TYPE, Double.TYPE
 };

 private static final Class<?>[] WRAPPER_TYPES = {
         Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
         Float.class, Double.class
 };

 private static final Object[] PRIMITIVE_VALUES = {
         false, '\u0000', (byte) 0, (short) 0, 0, 0L, 0F, 0D
 };

 private static final Object[] WRAPPER_RETURN_VALUES = {
         false, '\u0000', (byte) 0, (short) 0, 0, 0L, 0F, 0D
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> primitiveType = PRIMITIVE_TYPES[i];
         Class<?> actual = Primitives.primitiveTypeOf(primitiveType);
         Class<?> expected = primitiveType;
         Assert.assertEquals("primitiveTypeOf(PrimitiveType) should return itself.", expected, actual);
     }
     for (int i = 0; i < WRAPPER_TYPES.length; i++) {
         Class<?> wrapperType = WRAPPER_TYPES[i];
         Class<?> actual = Primitives.primitiveTypeOf(wrapperType);
         Class<?> expected = Primitives.primitiveTypeOf(wrapperType.getTypeParameters()[0]);
         Assert.assertEquals("primitiveTypeOf(WrapperType<T>) should return primitive type of type parameter T.", expected, actual);
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> primitiveType = PRIMITIVE_TYPES[i];
         boolean actual = Primitives.isPrimitiveWrapper(primitiveType);
         boolean expected = false;
         Assert.assertEquals("isPrimitiveWrapper(PrimitiveType) should return false.", expected, actual);
     }
     for (int i = 0; i < WRAPPER_TYPES.length; i++) {
         Class<?> wrapperType = WRAPPER_TYPES[i];
         boolean actual = Primitives.isPrimitiveWrapper(wrapperType);
         boolean expected = true;
         Assert.assertEquals("isPrimitiveWrapper(WrapperType<T>) should return true.", expected, actual);
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> primitiveType = PRIMITIVE_TYPES[i];
         Object actual = Primitives.primitiveWrapperOf(primitiveType);
         Object expected = PRIMITIVE_VALUES[i];
         if (primitiveType == boolean.class) {
             expected = Boolean.FALSE;
         }
         Assert.assertEquals("primitiveWrapperOf(PrimitiveType) should return corresponding primitive value wrapper.", expected, actual);
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> primitiveType = PRIMITIVE_TYPES[i];
         Object actual = Primitives.primitiveValueOrNullFor(primitiveType);
         Object expected = PRIMITIVE_VALUES[i];
         Assert.assertEquals("primitiveValueOrNullFor(PrimitiveType) should return corresponding primitive value.", expected, actual);
     }
 }

 @Test
 public void testWrapperReturnValuesMap() {
     for (int i = 0; i < WRAPPER_TYPES.length; i++) {
         Class<?> wrapperType = WRAPPER_TYPES[i];
         Object actual = Primitives.wrapperReturnValues.get(wrapperType);
         Object expected = WRAPPER_RETURN_VALUES[i];
         Assert.assertEquals("wrapperReturnValues should contain correct value for given wrapper type.", expected, actual);
     }
 }

 @Test
 public void testPrimitiveValuesMap() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> primitiveType = PRIMITIVE_TYPES[i];
         Object actual = Primitives.primitiveValues.get(primitiveType);
         Object expected = PRIMITIVE_VALUES[i];
         Assert.assertEquals("primitiveValues should contain correct value for given primitive type.", expected, actual);
     }
 }

}