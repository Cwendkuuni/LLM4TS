package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getField(SomeClass.class, "fieldName");
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 assertNull(new GenericMaster().getGenericType(null));
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Field field = getField(SomeClass.class, "nonGenericField");
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_multiLevelGenericType() {
 Field field = getField(SomeNestedClass.class, "nestedField");
 Class expectedType = Integer.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_noActualTypeArguments() {
 Field field = getField(NoArgumentsClass.class, "field");
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 private static Field getField(Class clazz, String fieldName) {
 try {
 return clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 static class SomeClass {
 private String fieldName;
 }

 static class SomeNestedClass<T> {
 private T nestedField;
 }

 static class NoArgumentsClass {
 //empty class with no generic type
 }
}