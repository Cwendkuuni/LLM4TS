package org.junit.Test;

import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() throws NoSuchFieldException {

 // Test field with single generic type
 Class clazz = TestClassWithSingleGenericType.class;
 Field field = clazz.getDeclaredField("fieldWithSingleGenericType");
 field.setAccessible(true);

 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(field);

 assertEquals("Test for single generic type", expectedType, actualType);

 // Test field without generic type
 field = clazz.getDeclaredField("fieldWithoutGenericType");
 field.setAccessible(true);

 assertNull("Test for non-generic type", genericMaster.getGenericType(field));

 // Test field with nested generic types
 Class clazzWithNestedGenerics = TestClassWithNestedGenerics.class;
 Field fieldWithNestedGenerics = clazzWithNestedGenerics.getDeclaredField("fieldWithNestedGenerics");
 fieldWithNestedGenerics.setAccessible(true);

 Class<?> expectedTypeForNestedGenerics = Integer.class;
 Class<?> actualTypeForNestedGenerics = (Class<?>) genericMaster.getGenericType(fieldWithNestedGenerics).getActualTypeArguments()[0];

 assertEquals("Test for nested generic types", expectedTypeForNestedGenerics, actualTypeForNestedGenerics);
 }

 private static class TestClassWithSingleGenericType {
 private String fieldWithSingleGenericType;
 }

 private static class TestClassWithNestedGenerics<X, Y> {
 private List<X> listWithSingleGenericTypeAndNestedGenerics;
 }
}