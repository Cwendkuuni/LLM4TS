package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field;
 try {
 field = MyClassWithGenericField.class.getField("myField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = String.class;
 Class actualType = GenericMaster.getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Field nonGenericField;
 try {
 nonGenericField = MyClassWithNonGenericField.class.getField("nonGenericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object.class;
 Class actualType = GenericMaster.getGenericType(nonGenericField);

 assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_nullField() {
 GenericMaster genericMaster = new GenericMaster();

 Field nullField = null;
 Class actualType = genericMaster.getGenericType(nullField);
 }

 @Test
 public void testGetGenericType_nestedGenericField() {
 Field nestedGenericField;
 try {
 nestedGenericField = MyClassWithNestedGenericField.class.getField("nestedGenericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Integer.class;
 Class actualType = GenericMaster.getGenericType(nestedGenericField);

 assertNotNull(actualType);
 assertEquals(expectedType.getName(), actualType.getName());
 }
}

 class MyClassWithGenericField {
 private String myField;
 }

 class MyClassWithNonGenericField {
 private int nonGenericField;
 }

 class MyClassWithNestedGenericField<T> {
 private List<T> nestedGenericField;
 }