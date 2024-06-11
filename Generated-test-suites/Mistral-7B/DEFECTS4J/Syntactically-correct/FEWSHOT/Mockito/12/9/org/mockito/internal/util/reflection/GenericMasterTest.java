package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField(Foo.class, "list");
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(List.class, genericType);

 field = getField(Bar.class, "map");
 genericType = GenericMaster.getGenericType(field);
 assertEquals(Map.class, genericType);

 field = getField(Baz.class, "set");
 genericType = GenericMaster.getGenericType(field);
 assertEquals(Set.class, genericType);

 field = getField(Quux.class, "array");
 genericType = GenericMaster.getGenericType(field);
 assertNotNull(genericType);
 assertEquals(String[].class, genericType);

 field = getField(Quux2.class, "array");
 genericType = GenericMaster.getGenericType(field);
 assertNotNull(genericType);
 assertEquals(Integer[].class, genericType);
 }

 private Field getField(Class clazz, String fieldName) {
 try {
 return clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 static class Foo {
 private List<String> list;
 }

 static class Bar {
 private Map<String, Integer> map;
 }

 static class Baz {
 private Set<Integer> set;
 }

 static class Quux {
 private String[] array;
 }

 static class Quux2<T> {
 private T[] array;
 }
}