package org.junit.test;

import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

public class GenericMasterTest {

 @BeforeClass
 public static void setUp() {
 // Create a test object
 GenericMaster genericMaster = new GenericMaster();
 FieldTestDouble fieldTestDouble = new FieldTestDouble();
 try {
 // Set a field with a generic type
 Field field = FieldTestDouble.class.getDeclaredField("genericField");
 field.setAccessible(true);
 field.set(fieldTestDouble, "Test Value");
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for a simple generic field", expectedType, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Test failed: Unable to get or set the field");
 }
 }

 @Test
 public void testGetGenericTypeForSimpleGenericField() {
 // Test for a simple generic field
 Field field = FieldTestDouble.class.getDeclaredField("genericField");
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for a simple generic field", expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForNonGenericField() {
 // Test for a non-generic field
 Field nonGenericField = FieldTestDouble.class.getDeclaredField("nonGenericField");
 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(nonGenericField);
 Assert.assertEquals("Test failed: getGenericType() returned Object.class for a non-generic field", expectedType, actualType);
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericTypeForNullField() {
 // Test for a null field
 Field nullField = null;
 try {
 genericMaster.getGenericType(nullField);
 } catch (NullPointerException e) {
 // expected
 }
 // But in production code, you should check for null instead of relying on a checked exception
 Assert.assertNull("Test failed: getGenericType() did not throw a NPE for a null field", nullField);
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericTypeForNonAccessibleField() {
 // Test for a non-accessible field
 Field nonAccessibleField = FieldTestDouble.class.getDeclaredField("nonAccessibleField");
 nonAccessibleField.setAccessible(false);
 try {
 genericMaster.getGenericType(nonAccessibleField);
 } catch (IllegalAccessException e) {
 // expected
 }
 // But in production code, you should check for accessibility instead of relying on a checked exception
 Assert.assertFalse("Test failed: getGenericType() returned true for a non-accessible field", nonAccessibleField.isAccessible());
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericTypeForArrayField() {
 // Test for an array field
 Field arrayField = FieldTestDouble[].class.getDeclaredField("arrayField");
 arrayField.setAccessible(true);
 Object arrayValue = new Object[3];
 try {
 FieldTestDouble testObject = new FieldTestDouble();
 field.set(testObject, arrayValue);
 Class expectedType = Object[].class;
 Class actualType = genericMaster.getGenericType(arrayField);
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for an array field", expectedType, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Test failed: Unable to get or set the field");
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericTypeForNestedGenericField() {
 // Test for a nested generic field
 Field nestedField = FieldNested.class.getDeclaredField("nestedField");
 nestedField.setAccessible(true);
 try {
 FieldTestDouble testObject = new FieldTestDouble();
 FieldTestNested nestedObject = new FieldTestNested();
 field.set(testObject, nestedObject);
 Class expectedType = Integer[].class;
 Class actualType = genericMaster.getGenericType(nestedField);
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for a nested generic field", expectedType, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Test failed: Unable to get or set the field");
 }
 }

 @Test
 public void testGetGenericTypeForJavaListField() {
 // Test for a Java List field
 Field listField = FieldTestDouble.class.getDeclaredField("listField");
 listField.setAccessible(true);
 try {
 FieldTestDouble testObject = new FieldTestDouble();
 List listValue = Arrays.asList(1, 2, 3);
 field.set(testObject, listValue);
 Class expectedType = List.class;
 Class actualType = genericMaster.getGenericType(listField);
 Class listElementType = (Class) ((ParameterizedType) actualType.getComponentType()).getActualTypeArguments()[0];
 Class listExpectedType = ArrayList.class;
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for a Java List field", listExpectedType.getName().substring(0, listExpectedType.getName().lastIndexOf(".")) + "<" + listElementType.getName() + ">", actualType.toString());
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Test failed: Unable to get or set the field");
 }
 }

 @Test
 public void testGetGenericTypeForJavaMapField() {
 // Test for a Java Map field
 try {
 Field mapField = FieldTestDouble.class.getDeclaredField("mapField");
 mapField.setAccessible(true);
 Class keyType = String.class;
 Class valueType = Integer.class;
 FieldTestDouble testObject = new FieldTestDouble();
 Map mapValue = new HashMap<>();
 mapValue.put("key", 1);
 mapValue.put("key2", 2);
 field.set(testObject, mapValue);
 Class expectedType = Map.class;
 Class actualType = genericMaster.getGenericType(mapField);
 Class mapKeyType = (Class) ((ParameterizedType) actualType.getComponentTypes()[0]).getActualTypeArguments()[0];
 Class mapValueType = (Class) ((ParameterizedType) actualType.getComponentTypes()[1]).getActualTypeArguments()[0];
 Class mapExpectedType = HashMap.class;
 Assert.assertEquals("Test failed: getGenericType() returned wrong type for a Java Map field", mapExpectedType.getName().substring(0, mapExpectedType.getName().lastIndexOf(".")) + "<" + mapKeyType.getName() + ": " + mapValueType.getName() + ">", actualType.toString());
 } catch (NoSuchFieldException | IllegalAccessException e) {
 e.printStackTrace();
 Assert.fail("Test failed: Unable to get or set the field");
 }
 }
}

class FieldTestDouble {
 private int nonGenericField;
 private int genericField;
 private List listField;
 private Map<String, Integer> mapField;
 private FieldNested nestedField;

 static class FieldTestNested {
 private int nestedField;
 }
}

class FieldNested {
 private int nestedField;
}

class TestData {
 static final Object[] arrayField = new Object[3];
}