package org.mockito.internal.util.reflection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.test.testobjects.FieldWithGenericType;
import org.mockito.internal.util.reflection.test.testobjects.FieldWithNonGenericType;

import static org.junit.Assert.*;

public class GenericMasterTest {

 private GenericMaster genericMaster;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericTypeForFieldWithGenericType() {
 Field field = FieldWithGenericType.class.getDeclaredField("field");
 field.setAccessible(true);
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForFieldWithNonGenericType() {
 Field field = FieldWithNonGenericType.class.getDeclaredField("field");
 field.setAccessible(true);
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForNullField() {
 assertNull(genericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericTypeForFieldWithArrayType() {
 Field field = FieldWithArrayType.class.getDeclaredField("field");
 field.setAccessible(true);
 Class expectedType = Integer[].class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForFieldWithNestedGenerics() {
 Field field = FieldWithNestedGenerics.class.getDeclaredField("field");
 field.setAccessible(true);
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeForFieldWithPrimitiveType() {
 Field field = Integer.TYPE.getDeclaredField("typeName");
 field.setAccessible(true);
 assertNull(genericMaster.getGenericType(field));
 }
}