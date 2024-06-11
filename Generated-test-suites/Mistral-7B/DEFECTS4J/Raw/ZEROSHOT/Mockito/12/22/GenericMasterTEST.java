package org.junit.Tests;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestHelper;  // Assuming you have a FieldTestHelper for creating fields and getters

import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
     GenericMaster genericMaster = new GenericMaster();

     // Test with a simple field of type Integer
     Field intField = FieldTestHelper.createField(SomeClassWithIntField.class, "intField");
     assertEquals(Integer.class, genericMaster.getGenericType(intField));

     // Test with a simple field of type String
     Field stringField = FieldTestHelper.createField(SomeClassWithStringField.class, "stringField");
     assertEquals(String.class, genericMaster.getGenericType(stringField));

     // Test with a field of a nested generic type
     Field nestedField = FieldTestHelper.createField(SomeNestedClass.class, "nestedField");
     assertEquals(NestedType.class, genericMaster.getGenericType(nestedField));

     // Test with a non-generic field
     Field nonGenericField = FieldTestHelper.createField(SomeClassWithoutGenericField.class, "nonGenericField");
     assertEquals(Object.class, genericMaster.getGenericType(nonGenericField));

     // Test with a null field
     assertNull(genericMaster.getGenericType(null));
 }

 // Assuming you have the following classes for testing
 static class SomeClassWithIntField {
     private int intField;
 }

 static class SomeClassWithStringField {
     private String stringField;
 }

 static class SomeNestedClass<T> {
     private T nestedField;
 }

 static class NestedType {
 }

 static class SomeClassWithoutGenericField {
     private Object nonGenericField;
 }
}