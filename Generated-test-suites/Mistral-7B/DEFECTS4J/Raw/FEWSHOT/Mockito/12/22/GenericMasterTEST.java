package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getDeclaredField("value");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(String.class, genericType);

 Class clazz2 = ArrayList.class;
 try {
 field = clazz2.getDeclaredField("elementData");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class<?> genericType2 = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, genericType2);

 Class<List<String>> expected = List.class;
 Class<List<String>> actual = new GenericMaster().getGenericType(expected.getDeclaredField("elementType"));
 assertEquals(String.class, actual.getComponentType());
 assertEquals(List.class, expected);
 assertEquals(List.class, actual.getDeclaringClass());
 }

 @Test
 public void testGetGenericType_nullField() {
 assertNull(new GenericMaster().getGenericType(null));
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 try {
 Class clazz = Integer.class;
 Field field = clazz.getDeclaredField("TYPE");
 new GenericMaster().getGenericType(field);
 } catch (NoSuchFieldException e) {
 //expected
 }
 }
}