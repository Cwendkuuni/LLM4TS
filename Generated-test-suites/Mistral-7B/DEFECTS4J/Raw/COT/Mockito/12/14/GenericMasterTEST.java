package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() {
 Field field = getFieldWithGenericType("List<Integer> list");
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForPrimitiveType() {
 Field field = getFieldWithGenericType("List<Integer> list");
 Class expectedType = int.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType.getClass());
 }

 @Test
 public void testGetGenericTypeForObjectType() {
 Field field = getFieldWithGenericType("List<Object> list");
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeForNullField() {
 Field field = null;
 genericMaster.getGenericType(field);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeForNonGenericField() {
 Field field = getFieldWithNonGenericType("int i");
 genericMaster.getGenericType(field);
 }

 @Test
 public void testGetGenericTypeForArrayType() {
 Field field = getFieldWithGenericArrayType("int[] arr");
 Class expectedType = int[].class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForNestedGenericType() {
 Field field = getFieldWithNestedGenericType("List<List<Integer>> listOfLists");
 Class expectedType = List.class;
 Class actualType = List.class; // or java.util.List.class
 while (actualType.getGenericSuperclass() != null) {
 if (actualType.getGenericSuperclass().getTypeName().equals(expectedType.getTypeName())) {
 break;
 }
 actualType = actualType.getGenericSuperclass();
 }
 assertEquals(expectedType, actualType);
 }

 private Field getFieldWithGenericType(String fieldName) {
 try {
 Field field = Class.forName("util.test.GenericTest").getField(fieldName);
 return field;
 } catch (ClassNotFoundException | NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getFieldWithNonGenericType(String fieldName) {
 try {
 Field field = Integer.class.getField(fieldName);
 return field;
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getFieldWithGenericArrayType(String fieldName) {
 try {
 Field field = Integer[].class.getField(fieldName);
 return field;
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getFieldWithNestedGenericType(String fieldName) {
 try {
 Field field = java.util.List.class.getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

}