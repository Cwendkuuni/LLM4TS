package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField(MyClass.class, "myField");
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 assertNull(new GenericMaster().getGenericType(null));
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Field field = getField(MyClass.class, "myIntField");
 assertNotNull(new GenericMaster().getGenericType(field));
 assertEquals(Integer.class, new GenericMaster().getGenericType(field));
 }

 @Test
 public void testGetGenericType_genericFieldWithOneTypeArgument() {
 Field field = getField(MyOtherClass.class, "myListField");
 Class expectedType = List.class;
 Type genericType = field.getGenericType();
 assertNotNull(genericType);
 assertEquals(List.class, genericType.getClass());
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_genericFieldWithTwoTypeArguments() {
 Field field = getField(MyOtherClass2.class, "myMapField");
 Class expectedType = Map.class;
 Type genericType = field.getGenericType();
 assertNotNull(genericType);
 assertEquals(Map.class, genericType.getClass());
 Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
 assertEquals(2, actualTypeArguments.length);
 Class firstArgument = (Class) actualTypeArguments[0];
 Class secondArgument = (Class) actualTypeArguments[1];
 Class keyType = String.class;
 Class valueType = Integer.class;
 assertEquals(keyType, firstArgument);
 assertEquals(valueType, secondArgument);
 }

 @Test
 public void testGetGenericType_deeplyNestedGenericField() {
 Field field = getField(MyOtherClass3.class, "myNestedListField");
 Class expectedType = List.class;
 Type genericType = field.getGenericType();
 assertNotNull(genericType);
 assertEquals(List.class, genericType.getClass());
 Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
 assertEquals(1, actualTypeArguments.length);
 Class nestedFieldType = (Class) actualTypeArguments[0];
 Field nestedField = getField(nestedFieldType, "myListField");
 Class nestedExpectedType = List.class;
 Class nestedActualType = new GenericMaster().getGenericType(nestedField);
 assertEquals(nestedExpectedType, nestedActualType);
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 private static <T> Field getField(Class clazz, String fieldName) {
 Field field;
 try {
 field = clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 field.setAccessible(true);
 return field;
 }

 class MyClass {
 private String myField;
 private int myIntField;
 }

 class MyOtherClass {
 private List<String> myListField;
 }

 class MyOtherClass2 {
 private Map<String, Integer> myMapField;
 }

 class MyOtherClass3 {
 private List<MyNestedClass> myNestedListField;

 static class MyNestedClass {
 private List<String> myListField;
 }
 }

}