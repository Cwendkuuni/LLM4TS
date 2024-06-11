package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField(SomeClass.class, "someField");
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);

 field = getField(SomeNestedClass.class, "someNestedField");
 expectedType = List.class;
 Type genericType = field.getGenericType();
 ParameterizedType parametrizedType = (ParameterizedType) genericType;
 Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
 assertEquals(expectedType, actualTypeArguments[0].getClass());

 field = getField(SomeOtherClass.class, "someOtherField");
 assertNull(new GenericMaster().getGenericType(field));
 }

 private Field getField(Class clazz, String fieldName) {
 try {
 return clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 static class SomeClass {
 private String someField;
 }

 static class SomeNestedClass<T> {
 private List<T> someNestedField;
 }

 static class SomeOtherClass {
 // no generic field
 }
}