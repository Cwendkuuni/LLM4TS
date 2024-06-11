package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getField("class");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expected = String.class;
 Class actual = new GenericMaster().getGenericType(field);
 assertEquals(expected, actual);

 clazz = List.class;
 try {
 field = clazz.getField("type");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Type genericType = field.getGenericType();
 assertNotNull(genericType);
 assertInstanceOf(ParameterizedType.class, genericType);

 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
 assertEquals(1, actualTypeArguments.length);
 Class actual = (Class) actualTypeArguments[0];
 assertEquals(String.class, actual);
 }

 @Test
 public void testGetGenericTypeForEmptyArray() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getField("class");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actual = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeForNullField() {
 new GenericMaster().getGenericType(null);
 }
}