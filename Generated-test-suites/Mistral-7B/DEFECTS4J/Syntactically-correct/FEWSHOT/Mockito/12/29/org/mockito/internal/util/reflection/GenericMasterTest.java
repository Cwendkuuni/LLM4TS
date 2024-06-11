package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField("java.util.ArrayList", "elementData");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, genericType);

 field = getField("java.util.ArrayList<String>", "elementData");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(String.class, genericType);

 field = getField("java.util.LinkedHashSet<Integer>", "elementData");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(Integer.class, genericType);
 }

 @Test
 public void testGetGenericTypeWithNestedGenerics() {
 Field field = getField("java.util.LinkedHashMap<String, java.util.List<Integer>>", "entrySet");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.List.class, genericType);

 Class listType = getClassWithGenericType(java.util.List.class, Integer.class);
 Class mapType = getClassWithGenericType(java.util.LinkedHashMap.class, String.class, listType);
 assertEquals(mapType, genericType);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 Field nullField = null;
 Class genericType = new GenericMaster().getGenericType(nullField);
 assertNull(genericType);
 }

 @Test
 public void testGetGenericTypeWithPrimitiveType() {
 Field field = getField("java.util.ArrayList<int[]>", "elementData");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(int[].class, genericType);
 }

 @Test
 public void testGetGenericTypeWithArray() {
 Field field = getField("java.lang.reflect.Field[]", "fields");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(Field[].class, genericType);
 }

 private Field getField(String className, String fieldName) {
 try {
 Class<?> clazz = Class.forName(className);
 Field field = clazz.getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 } catch (ClassNotFoundException | NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Class getClassWithGenericType(Class clazz, Class... typeArguments) {
 try {
 return (Class) clazz.getGenericSuperclass();
 } catch (NullPointerException e) {
 if (clazz.isInterface()) {
 return Object.class;
 } else {
 throw new RuntimeException("Could not find generic superclass for " + clazz.getName(), e);
 }
 }

 Type[] types = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
 for (Type type : types) {
 if (type instanceof Class) {
 Class castedType = (Class) type;
 if (isAssignableFrom(castedType, typeArguments[0])) {
 return castedType;
 }
 }
 }
 return null;
 }

 private boolean isAssignableFrom(Class clazz, Class other) {
 Class superclass = clazz.getSuperclass();
 if (other.isPrimitive()) {
 return clazz.isPrimitive() && other.equals(clazz.getPrimitiveClass());
 } else {
 return clazz.isAssignableFrom(other);
 }
 }

}