package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getClass().getDeclaredField("fieldWithGenericType"); // replace "fieldWithGenericType" with actual field name
 Class expectedType = String.class; // replace with expected type
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_fieldWithoutGenericType() {
 Field field = getClass().getDeclaredField("fieldWithoutGenericType"); // replace "fieldWithoutGenericType" with actual field name
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_fieldWithArrayType() throws NoSuchFieldException {
 Field field = getClass().getDeclaredField("fieldWithArrayType"); // replace "fieldWithArrayType" with actual field name
 Class expectedType = int[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_fieldWithNestedGenerics() throws NoSuchFieldException {
 Field field = getClass().getDeclaredField("fieldWithNestedGenerics"); // replace "fieldWithNestedGenerics" with actual field name
 Class expectedType = List.class; // replace with expected type
 Type type = field.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) type;
 Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
 Class innerListType = (Class) actualTypeArguments[0];
 Class listType = (Class) actualTypeArguments[1];
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 assertEquals(innerListType, List.class);
 assertEquals(listType, String.class);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_nullField() {
 Field nullField = null;
 new GenericMaster().getGenericType(nullField);
 }
}