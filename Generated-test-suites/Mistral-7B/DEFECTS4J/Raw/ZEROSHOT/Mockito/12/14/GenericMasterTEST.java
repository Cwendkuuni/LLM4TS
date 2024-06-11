package org.junit.junit4; // Assuming you are using JUnit 4 for testing

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 GenericMaster genericMaster = new GenericMaster();

 // Test with a generic field
 try {
 Field field = GenericMaster.class.getDeclaredField("someField"); // Assuming there is a generic field named "someField"
 field.setAccessible(true);
 Class expectedType = String.class; // Replace this with the expected generic type for your test
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Failed to access test field");
 }

 // Test with a non-generic field
 try {
 Field nonGenericField = GenericMaster.class.getDeclaredField("nonGenericField"); // Assuming there is a non-generic field named "nonGenericField"
 nonGenericField.setAccessible(true);
 Class actualType = genericMaster.getGenericType(nonGenericField);
 Assert.assertEquals(Object.class, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Failed to access test field");
 }
 }
}