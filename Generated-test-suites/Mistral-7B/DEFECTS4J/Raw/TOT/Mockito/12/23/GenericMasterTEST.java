package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 // Test case for getGenericType method with a generic field
 @Test
 public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
     GenericMaster genericMaster = new GenericMaster();
     Class clazz = int[].class;
     Field field = clazz.getDeclaredField("length");
     field.setAccessible(true);
     Class expectedResult = Integer.TYPE;
     Class actualResult = genericMaster.getGenericType(field);
     assertEquals(expectedResult, actualResult);
 }

 // Test case for getGenericType method with a non-generic field
 @Test
 public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
     GenericMaster genericMaster = new GenericMaster();
     Class clazz = String.class;
     Field field = clazz.getDeclaredField("length"); // this field doesn't exist
     field.setAccessible(true); // this line will throw an exception

     try {
         // this test case is for error handling
         Field invalidField = clazz.getDeclaredField("invalidFieldName");
         invalidField.setAccessible(true); // this line will throw an exception
         assertThrows(NoSuchFieldException.class, () -> genericMaster.getGenericType(invalidField));
     } catch (NoSuchFieldException e) {
         // this exception is expected
     }

     Class actualResult = genericMaster.getGenericType(field);
     assertEquals(Object.class, actualResult);
 }
}