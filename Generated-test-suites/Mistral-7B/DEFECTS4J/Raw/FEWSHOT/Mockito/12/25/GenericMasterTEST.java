package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericTypeForSimpleField() {
 Field field = getField(SomeClass.class, "simpleField");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(String.class, genericType);
 }

 @Test
 public void testGetGenericTypeForGenericField() {
 Field field = getField(SomeGenericClass.class, "genericField");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(List.class, genericType);
 }

 @Test
 public void testGetGenericTypeForNullField() {
 assertNull(new GenericMaster().getGenericType(null));
 }

 @Test
 public void testGetGenericTypeForNonGenericField() {
 Field field = getField(SomeClass.class, "nonGenericField");
 Class genericType = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, genericType);
 }

 @Test
 public void testGetGenericTypeForStaticField() {
 Field field = getField(SomeClass.class, "staticField");
 assertFalse(Modifier.isStatic(field.getModifiers()));
 try {
 new GenericMaster().getGenericType(field);
 fail("Should not reach here");
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 private Field getField(Class clazz, String name) {
 try {
 return clazz.getDeclaredField(name);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 static class SomeClass {
 private String simpleField;
 }

 static class SomeGenericClass<T> {
 private List<T> genericField;
 }
}