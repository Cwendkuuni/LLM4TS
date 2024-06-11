package org.mockito.internal.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

 private static final Map<Class<?>, Object> WRAPPER_RETURN_VALUES = Primitives.wrapperReturnValues;
 private static final Map<Class<?>, Object> PRIMITIVE_VALUES = Primitives.primitiveValues;

 @Test
 public void testPrimitiveTypeOf() {
 final Class<Byte> byteClass = Byte.class;
 final Class<Integer> integerClass = Integer.class;
 final Class<String> stringClass = String.class;

 assertEquals(byteClass, Primitives.primitiveTypeOf(byteClass));
 assertEquals(byte.class, Primitives.primitiveTypeOf(byteClass.asSubclass(Byte.TYPE)));
 assertEquals(Byte.TYPE, Primitives.primitiveTypeOf(byte.class));
 assertEquals(integerClass, Primitives.primitiveTypeOf(integerClass));
 assertEquals(Integer.TYPE, Primitives.primitiveTypeOf(integerClass.asSubclass(Integer.TYPE)));
 assertNull(Primitives.primitiveTypeOf(stringClass));
 }

 @Test
 public void testIsPrimitiveWrapper() {
 final Class<Boolean> booleanWrapperClass = Boolean.class;
 final Class<Byte> byteWrapperClass = Byte.class;
 final Class<Integer> integerWrapperClass = Integer.class;
 final Class<String> stringClass = String.class;

 assertTrue(Primitives.isPrimitiveWrapper(booleanWrapperClass));
 assertTrue(Primitives.isPrimitiveWrapper(Byte.class));
 assertTrue(Primitives.isPrimitiveWrapper(byte.class));
 assertFalse(Primitives.isPrimitiveWrapper(integerWrapperClass));
 assertFalse(Primitives.isPrimitiveWrapper(String.class));
 }

 @Test
 public void testPrimitiveWrapperOf() {
 final Class<Boolean> booleanWrapperClass = Boolean.class;
 final Class<Byte> byteWrapperClass = Byte.class;
 final Class<Integer> integerWrapperClass = Integer.class;
 final Class<String> stringClass = String.class;

 assertEquals(false, Primitives.primitiveWrapperOf(booleanWrapperClass));
 assertEquals((byte) 0, Primitives.primitiveWrapperOf(byteWrapperClass));
 assertEquals(0, Primitives.primitiveWrapperOf(integerWrapperClass.asSubclass(Integer.TYPE)));
 assertNull(Primitives.primitiveWrapperOf(stringClass));
 }

 @Test
 public void testPrimitiveValueOrNullFor() {
 final Class<Boolean> booleanClass = Boolean.class;
 final Class<Byte> byteClass = Byte.class;
 final Class<Short> shortClass = Short.class;
 final Class<Integer> intClass = Integer.class;
 final Class<Float> floatClass = Float.class;
 final Class<Double> doubleClass = Double.class;

 assertEquals(false, Primitives.primitiveValueOrNullFor(booleanClass));
 assertEquals((byte) 0, Primitives.primitiveValueOrNullFor(byteClass));
 assertEquals((short) 0, Primitives.primitiveValueOrNullFor(shortClass));
 assertEquals(0, Primitives.primitiveValueOrNullFor(intClass));
 assertEquals(0F, Primitives.primitiveValueOrNullFor(floatClass));
 assertEquals(0D, Primitives.primitiveValueOrNullFor(doubleClass));
 }

 @Test
 public void testWrapperTypesMap() {
 final Class<Boolean> booleanWrapperClass = Boolean.class;
 final Class<Byte> byteWrapperClass = Byte.class;
 final Class<Short> shortWrapperClass = Short.class;
 final Class<Integer> intWrapperClass = Integer.class;
 final Class<Long> longWrapperClass = Long.class;
 final Class<Float> floatWrapperClass = Float.class;
 final Class<Double> doubleWrapperClass = Double.class;

 assertEquals(Boolean.TYPE, Primitives.wrapperTypes.get(booleanWrapperClass));
 assertEquals(Byte.TYPE, Primitives.wrapperTypes.get(byteWrapperClass));
 assertEquals(Short.TYPE, Primitives.wrapperTypes.get(shortWrapperClass));
 assertEquals(Integer.TYPE, Primitives.wrapperTypes.get(intWrapperClass));
 assertEquals(Long.TYPE, Primitives.wrapperTypes.get(longWrapperClass));
 assertEquals(Float.TYPE, Primitives.wrapperTypes.get(floatWrapperClass));
 assertEquals(Double.TYPE, Primitives.wrapperTypes.get(doubleWrapperClass));
 }

 @Test
 public void testPrimitiveTypesMap() {
 final Class<Boolean> booleanClass = Boolean.class;
 final Class<Byte> byteClass = Byte.class;
 final Class<Short> shortClass = Short.class;
 final Class<Integer> intClass = Integer.class;
 final Class<Long> longClass = Long.class;
 final Class<Float> floatClass = Float.class;
 final Class<Double> doubleClass = Double.class;

 assertEquals(Boolean.TYPE, Primitives.primitiveTypes.get(booleanClass));
 assertEquals(Byte.TYPE, Primitives.primitiveTypes.get(byteClass));
 assertEquals(Short.TYPE, Primitives.primitiveTypes.get(shortClass));
 assertEquals(Integer.TYPE, Primitives.primitiveTypes.get(intClass));
 assertEquals(Long.TYPE, Primitives.primitiveTypes.get(longClass));
 assertEquals(Float.TYPE, Primitives.primitiveTypes.get(floatClass));
 assertEquals(Double.TYPE, Primitives.primitiveTypes.get(doubleClass));
 }

 @Test
 public void testWrapperReturnValuesMap() {
 final Class<Boolean> booleanWrapperClass = Boolean.class;
 final Class<Byte> byteWrapperClass = Byte.class;
 final Class<Short> shortWrapperClass = Short.class;
 final Class<Integer> intWrapperClass = Integer.class;
 final Class<Long> longWrapperClass = Long.class;
 final Class<Float> floatWrapperClass = Float.class;
 final Class<Double> doubleWrapperClass = Double.class;

 assertEquals(false, Primitives.wrapperReturnValues.get(booleanWrapperClass));
 assertEquals((byte) 0, Primitives.wrapperReturnValues.get(byteWrapperClass).byteValue());
 assertEquals((short) 0, Primitives.wrapperReturnValues.get(shortWrapperClass).shortValue());
 assertEquals(0, Primitives.wrapperReturnValues.get(intWrapperClass).intValue());
 assertEquals(0L, Primitives.wrapperReturnValues.get(longWrapperClass).longValue());
 assertEquals(0F, Primitives.wrapperReturnValues.get(floatWrapperClass).floatValue());
 assertEquals(0D, Primitives.wrapperReturnValues.get(doubleWrapperClass).doubleValue());
 }

 @Test
 public void testPrimitiveValuesMap() {
 final Class<Boolean> booleanClass = Boolean.class;
 final Class<Byte> byteClass = Byte.class;
 final Class<Short> shortClass = Short.class;
 final Class<Integer> intClass = Integer.class;
 final Class<Long> longClass = Long.class;
 final Class<Float> floatClass = Float.class;
 final Class<Double> doubleClass = Double.class;

 assertEquals(false, Primitives.primitiveValues.get(booleanClass));
 assertEquals((byte) 0, Primitives.primitiveValues.get(byteClass).byteValue());
 assertEquals((short) 0, Primitives.primitiveValues.get(shortClass).shortValue());
 assertEquals(0, Primitives.primitiveValues.get(intClass).intValue());
 assertEquals(0L, Primitives.primitiveValues.get(longClass).longValue());
 assertEquals(0F, Primitives.primitiveValues.get(floatClass).floatValue());
 assertEquals(0D, Primitives.primitiveValues.get(doubleClass).doubleValue());
 }
}