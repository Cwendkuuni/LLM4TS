package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

 field = getField("com.example.MyClass<String>", "myField");
 genericType = new GenericMaster().getGenericType(field);
 assertEquals(java.lang.String.class, genericType);

 field = getField("java.util.HashMap<Integer, java.util.List<String>>", "map");
 Type genericTypeType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
 Type innerGenericType = ((ParameterizedType) genericTypeType).getActualTypeArguments()[0];
 Class innerClass = (Class) innerGenericType;
 assertEquals(java.util.List.class, innerClass.getComponentType());
 assertEquals(java.lang.Integer.class, ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1].getTypeName().substring(1, field.getGenericType().getTypeName().length() - 1).split("<")[0].getClass());
 Class outerClass = (Class) genericTypeType.getTypeName().substring(1, genericTypeType.getTypeName().length() - 1).split("<")[0];
 assertEquals(java.util.HashMap.class, outerClass);
 }

 private static Field getField(String className, String fieldName) {
 try {
 return Class.forName(className).getField(fieldName);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
}