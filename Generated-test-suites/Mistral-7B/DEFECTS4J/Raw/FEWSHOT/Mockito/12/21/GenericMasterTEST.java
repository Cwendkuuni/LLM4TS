package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getField("genericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Integer.class;
 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_whenFieldIsNotGeneric_thenReturnsObject() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getField("fieldName");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(Object.class, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_whenFieldIsNull_thenThrowsNullPointerException() {
 Field field = null;
 Class actualType = GenericMaster.getGenericType(field);
 }

 @Test
 public void testGetGenericType_whenFieldIsParameterizedWithMultipleTypes_thenReturnsFirstType() throws NoSuchFieldException {
 Class clazz = Map.class;
 Field field;
 try {
 field = clazz.getDeclaredField("entrySet");
 field.setAccessible(true);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Type genericType = field.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Type[] actualTypes = parameterizedType.getActualTypeArguments();
 Class expectedType = Map.Entry.class;
 Class actualType = (Class) actualTypes[0];
 assertEquals(expectedType, actualType);
 }
}