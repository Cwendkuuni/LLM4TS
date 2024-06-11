package org.junit.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() {
     try {
         Class clazz = TestClassWithGenericField.class;
         Field field = clazz.getDeclaredField("genericField");
         field.setAccessible(true);

         Class expectedResult = String.class;
         Class actualResult = genericMaster.getGenericType(field);

         Assert.assertEquals(expectedResult, actualResult);
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method.");
     }
 }

 @Test
 public void testGetGenericTypeWhenFieldIsNotGeneric() {
     try {
         Class clazz = TestClassWithoutGenericField.class;
         Field field = clazz.getDeclaredField("nonGenericField");
         field.setAccessible(true);

         Class expectedResult = Object.class;
         Class actualResult = genericMaster.getGenericType(field);

         Assert.assertEquals(expectedResult, actualResult);
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method with non-generic field.");
     }
 }

 @Test
 public void testGetGenericTypeWhenFieldIsPrivate() {
     try {
         Class clazz = TestClassWithPrivateGenericField.class;
         Field field = clazz.getDeclaredField("privateGenericField");
         field.setAccessible(true);

         Modifier modifiers = field.getModifiers();
         Assert.assertTrue("Field should be private.", Modifier.isPrivate(modifiers));

         Class expectedResult = Integer.class;
         Class actualResult = genericMaster.getGenericType(field);

         Assert.assertEquals(expectedResult, actualResult);
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method with private field.");
     }
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWhenFieldIsNull() {
     GenericMaster genericMaster = new GenericMaster();
     Assert.assertNull("getGenericType should return null when field is null.", genericMaster.getGenericType(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeWhenFieldHasMultipleTypeArguments() {
     try {
         Class clazz = TestClassWithMultipleGenericArguments.class;
         Field field = clazz.getDeclaredField("multipleGenericField");
         field.setAccessible(true);

         Class expectedExceptionType = IllegalArgumentException.class;
         Assert.assertThrows(expectedExceptionType, () -> {
             genericMaster.getGenericType(field);
         });
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method with multiple type arguments in field.");
     }
 }

 @Test
 public void testGetGenericTypeWhenFieldIsStatic() {
     try {
         Class clazz = TestClassWithStaticGenericField.class;
         Field field = clazz.getDeclaredField("staticGenericField");
         field.setAccessible(true);

         Class expectedResult = Boolean.class;
         Class actualResult = genericMaster.getGenericType(field);

         Assert.assertEquals(expectedResult, actualResult);
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method with static field.");
     }
 }

 @Test
 public void testGetGenericTypeWhenFieldIsFinal() {
     try {
         Class clazz = TestClassWithFinalGenericField.class;
         Field field = clazz.getDeclaredField("finalGenericField");
         field.setAccessible(true);

         Modifier modifiers = field.getModifiers();
         Assert.assertTrue("Field should be final.", Modifier.isFinal(modifiers));

         Class expectedResult = Double.class;
         Class actualResult = genericMaster.getGenericType(field);

         Assert.assertEquals(expectedResult, actualResult);
     } catch (NoSuchFieldException | IllegalAccessException e) {
         e.printStackTrace();
         Assert.fail("Error occurred while testing getGenericType method with final field.");
     }
 }
}

class TestClassWithGenericField {
 private String genericField;
}

class TestClassWithoutGenericField {
 private int nonGenericField;
}

class TestClassWithPrivateGenericField {
 private int privateGenericField;
}

class TestClassWithMultipleGenericArguments {
 private List<String> multipleGenericField;
}

class TestClassWithStaticGenericField {
 private static boolean staticGenericField;
}

class TestClassWithFinalGenericField {
 private final double finalGenericField;
}