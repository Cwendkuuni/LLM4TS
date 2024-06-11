package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrimitivesTest {

 private static final Class[] PRIMITIVE_TYPES = new Class[]{
         Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE,
         Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
 };

 private static final Class[] WRAPPER_TYPES = new Class[]{
         Boolean.class, Character.class, Byte.class, Short.class,
         Integer.class, Long.class, Float.class, Double.class
 };

 private static final Object[] PRIMITIVE_VALUES = new Object[]{
         false, '\u0000', (byte) 0, (short) 0,
         0, 0L, 0F, 0D
 };

 private static final Object[] WRAPPER_VALUES = new Object[]{
         false, '\u0000', (byte) 0, (short) 0,
         0, 0L, 0F, 0D
 };

 @Test
 public void testPrimitiveTypeOf() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Class<?> clazz = Primitives.classForName("java.lang." + PRIMITIVE_TYPES[i].getName());
         Assert.assertEquals(PRIMITIVE_TYPES[i], Primitives.primitiveTypeOf(clazz));
     }
 }

 @Test
 public void testIsPrimitiveWrapper() {
     for (int i = 0; i < WRAPPER_TYPES.length; i++) {
         Assert.assertTrue(Primitives.isPrimitiveWrapper(WRAPPER_TYPES[i]));
     }
 }

 @Test
 public void testPrimitiveWrapperOf() {
     for (int i = 0; i < WRAPPER_TYPES.length; i++) {
         Object wrapper = Primitives.primitiveWrapperOf(WRAPPER_TYPES[i]);
         Assert.assertEquals(PRIMITIVE_VALUES[i], wrapper);
     }
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
     for (int i = 0; i < PRIMITIVE_TYPES.length; i++) {
         Assert.assertEquals(PRIMITIVE_VALUES[i], Primitives.primitiveValueOrNullFor(PRIMITIVE_TYPES[i]));
     }
 }

 @Test(expected = NullPointerException.class)
 public void testNullParameter() {
     Assert.assertNull(Primitives.primitiveTypeOf(null));
     Assert.assertNull(Primitives.primitiveValueOrNullFor(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidParameterType() {
     Assert.assertNull(Primitives.primitiveTypeOf(String.class));
     Assert.assertNull(Primitives.primitiveValueOrNullFor(String.class));
 }
}