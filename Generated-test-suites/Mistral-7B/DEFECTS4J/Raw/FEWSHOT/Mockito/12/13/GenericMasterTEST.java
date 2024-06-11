package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field1 = getField("java.util.ArrayList", "elementData");
 assertEquals(Object[].class, GenericMaster.getGenericType(field1));

 Field field2 = getField("com.example.MyClass<String>", "myField");
 assertEquals(String.class, GenericMaster.getGenericType(field2));

 Field field3 = getField("com.example.MyNestedClass<Integer, String>", "nestedField");
 assertEquals(Integer.class, GenericMaster.getGenericType(field3));
 }

 private Field getField(String className, String fieldName) {
 try {
 Class clazz = Class.forName(className);
 Field field = clazz.getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }
}