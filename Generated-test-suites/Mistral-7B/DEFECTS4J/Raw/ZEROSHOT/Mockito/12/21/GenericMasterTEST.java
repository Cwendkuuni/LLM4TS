package org.junit.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // Test case for a generic field with a single type argument
 GenericMaster genericMaster = new GenericMaster();
 Class clazz = FieldTestDouble.class;
 Field field = clazz.getDeclaredField("field");
 field.setAccessible(true);
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);

 // Test case for a non-generic field
 clazz = FieldTestDouble.class;
 field = clazz.getDeclaredField("nonGenericField");
 field.setAccessible(true);
 assertNotNull(genericMaster.getGenericType(field));
 equals(Object.class, genericMaster.getGenericType(field));

 // Test case for a generic field with multiple type arguments
 clazz = FieldWithTwoGenerics.class;
 field = clazz.getDeclaredField("fieldWithTwoGenerics");
 field.setAccessible(true);
 Class<?>[] expectedTypes = { Integer.class, String.class };
 Class<?>[] actualTypes = genericMaster.getGenericType(field).getTypeArgs();
 assertArrayEquals(expectedTypes, actualTypes);
 }

 private static class FieldTestDouble {
 private Integer field;
 }

 private static class FieldWithTwoGenerics<T, S> {
 private T fieldWithTwoGenerics;
 }
}