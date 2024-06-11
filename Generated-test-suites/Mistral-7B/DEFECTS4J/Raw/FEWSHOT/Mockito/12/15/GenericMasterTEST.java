package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField("java.util.ArrayList", "elementData");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(Object[].class, genericType);

 field = getField("java.util.ArrayList<String>", "elementData");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(String[].class, genericType);

 field = getField("java.util.HashMap<String, Integer>", "entrySet");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.Map.Entry[].class, genericType);

 field = getField("java.util.LinkedHashMap<String, Integer>", "entrySet");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.Map.Entry[].class, genericType);

 field = getField("java.util.List", "list"); // field does not have generic type
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, genericType);
 }

 @Test
 public void testGetGenericTypeWithDeepNestedTypes() {
 Class clazz = java.util.ArrayList.class;
 Field field = getField(clazz, "element");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.Object.class, genericType); // ArrayList's element type is Object by default

 clazz = java.util.ArrayList.class;
 clazz = java.util.ArrayList.class.getGenericSuperclass();
 Type genericSuperclass = clazz.getGenericSuperclass();
 ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
 Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
 clazz = (Class) actualTypeArguments[0];

 field = getField(clazz, "element");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.Object.class, genericType); // ArrayList's element type is Object by default

 clazz = java.util.ArrayList.class;
 clazz = java.util.ArrayList.class.getGenericSuperclass();
 parameterizedType = (ParameterizedType) genericSuperclass;
 actualTypeArguments = parameterizedType.getActualTypeArguments();
 clazz = (Class) actualTypeArguments[1];

 field = getField(clazz, "T"); // Type variable of ArrayList's generic superclass, LinkList
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.Object.class, genericType); // LinkList's type parameter is Object by default

 clazz = java.util.LinkList.class;
 field = getField(clazz, "first");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.Object.class, genericType); // LinkedList's first node is an Object by default
 }

 private Field getField(Class clazz, String name) {
 try {
 return clazz.getDeclaredField(name);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }
}