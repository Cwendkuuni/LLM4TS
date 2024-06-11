package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getDeclaredField("value");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(String.class, genericType);

 clazz = List.class;
 field = null;
 try {
 field = clazz.getDeclaredField("list");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class<?> genericTypeList = new GenericMaster().getGenericType(field);
 assertEquals(List.class, genericTypeList);

 Class<?> genericTypeListString = new GenericMaster().getGenericType(new GenericMaster().getDeclaredField(List.class, "newArrayList"));
 assertEquals(java.util.List.class);
 assertEquals(java.lang.String.class, ((ParameterizedType) genericTypeList.getComponentType()).getActualTypeArguments()[0]);

 Class<?> genericTypeMap = new GenericMaster().getGenericType(new GenericMaster().getDeclaredField(Map.class, "newHashMap"));
 assertEquals(java.util.Map.class);
 assertEquals(java.lang.String.class, ((ParameterizedType) ((ParameterizedType) genericTypeMap.getComponentType()).getActualTypeArguments()[0]).getActualTypeArguments()[1]);
 assertEquals(java.lang.Object.class, ((ParameterizedType) genericTypeMap.getComponentType()).getActualTypeArguments()[0].getComponentType());
 }

 @Test
 public void testGetGenericTypeNullField() {
 Class genericType = new GenericMaster().getGenericType(null);
 assertNull(genericType);
 }

 @Test
 public void testGetGenericTypeNonGenericField() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getDeclaredField("length");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(Integer.TYPE, genericType);
 }
}