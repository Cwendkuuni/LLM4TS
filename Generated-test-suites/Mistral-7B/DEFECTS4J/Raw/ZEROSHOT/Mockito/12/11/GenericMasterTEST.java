package org.junit.junit4; //Replace this with the package of your test class if needed

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldNotFoundException;

import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException, FieldNotFoundException {
 // Test with a public field
 Class clazz = SomeClass.class;
 Field field = clazz.getDeclaredField("myField");
 field.setAccessible(true);
 GenericMaster gm = new GenericMaster();
 Class expectedType = String.class;
 Class returnedType = gm.getGenericType(field);
 Assert.assertEquals(expectedType, returnedType);

 // Test with a non-generic field
 Class nonGenericClass = Object.class;
 Field nonGenericField = nonGenericClass.getDeclaredField("nonExistentGenericType");
 nonGenericField.setAccessible(true);
 try {
 gm.getGenericType(nonGenericField);
 Assert.fail("Expected FieldNotFoundException but got none");
 } catch (FieldNotFoundException e) {
 // Expected
 }

 // Test with a private field
 clazz = SomeClass.class;
 field = clazz.getDeclaredField("privateField");
 field.setAccessible(true);
 expectedType = Integer.class;
 returnedType = gm.getGenericType(field);
 Assert.assertEquals(expectedType, returnedType);

 // Test with a null field
 try {
 gm.getGenericType(null);
 Assert.fail("Expected NullPointerException but got none");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = FieldNotFoundException.class)
 public void testGetGenericTypeWithNonExistentField() throws NoSuchFieldException {
 Class clazz = SomeClass.class;
 Field nonExistentField = clazz.getDeclaredField("nonExistentField");
 nonExistentField.setAccessible(true);
 GenericMaster gm = new GenericMaster();
 gm.getGenericType(nonExistentField);
 }

 @Test
 public void testGetGenericTypeWithNestedGenericTypes() throws NoSuchFieldException {
 Class clazz = NestedGenericClass.class;
 Field field = null;
 try {
 field = clazz.getDeclaredField("field");
 } catch (NoSuchFieldException e) {
 throw new FieldNotFoundException("Field not found: " + e.getMessage());
 }
 field.setAccessible(true);
 GenericMaster gm = new GenericMaster();
 Class expectedType = Integer.class;
 Class returnedType = gm.getGenericType(field);
 Assert.assertEquals(expectedType, returnedType);
 }

 // Add more test cases as needed
}