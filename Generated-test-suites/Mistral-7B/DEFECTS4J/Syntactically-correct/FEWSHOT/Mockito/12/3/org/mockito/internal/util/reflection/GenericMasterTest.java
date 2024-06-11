package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField("java.util.ArrayList", "list");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.List.class, genericType);

 field = getField("java.util.HashMap", "table");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.Map.class, genericType);

 field = getField("java.util.ArrayList<String>", "value");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.String.class, genericType);

 field = getField("java.util.HashMap<String, Integer>", "table");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.util.Map.class, genericType);
 assertEquals(java.lang.String.class, new GenericMaster().getGenericType(getField("java.util.Map.Entry<String, Integer>", "key")).getComponentType());
 assertEquals(java.lang.Integer.class, new GenericMaster().getGenericType(getField("java.util.Map.Entry<String, Integer>", "value")).getComponentType());

 field = getField("java.lang.reflect.ParameterizedType", "rawType");
 Type type = field.getGenericType();
 assertNotNull(type);
 assertEquals(java.lang.reflect.Map.class, type);

 field = getField("java.lang.reflect.Map.Entry<String, Integer>.value", "value");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.Integer.class, genericType);
 }

 private static Field getField(String className, String fieldName) {
 try {
 Class<?> clazz = Class.forName(className);
 Field field = clazz.getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }
}