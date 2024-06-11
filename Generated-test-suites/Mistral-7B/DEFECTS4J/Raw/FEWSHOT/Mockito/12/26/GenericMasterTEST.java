package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

 private static final class TestClass<T, U> {
 private Field field;
 }

 @Test
 public void testGetGenericType() {
 Class<TestClass<String, Integer>> clazz = TestClass.class;
 try {
 Field field = clazz.getDeclaredField("field");
 field.setAccessible(true);
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(String.class, genericType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new AssertionError(e);
 }

 Class<TestClass<Integer, String>> anotherClazz = TestClass.class;
 try {
 Field field = anotherClazz.getDeclaredField("field");
 field.setAccessible(true);
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(Integer.class, genericType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new AssertionError(e);
 }

 Class<?> ungenericClass = TestClass.class;
 try {
 Field field = ungenericClass.getDeclaredField("field");
 field.setAccessible(true);
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(Object.class, genericType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new AssertionError(e);
 }
 }

 @Test
 public void testGetGenericTypeForPrivateField() {
 Class<TestClass<String, Integer>> clazz = TestClass.class;
 TestClass instance = new TestClass();
 try {
 Field field = clazz.getDeclaredField("field");
 field.setAccessible(true);
 field.set(instance, "test");
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(String.class, genericType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new AssertionError(e);
 }
 }

 @Test
 public void testGetGenericTypeForStaticField() {
 Class<TestClass<String, Integer>> clazz = TestClass.class;
 try {
 Field field = clazz.getDeclaredField("FIELD");
 field.setAccessible(true);
 Class<?> modifiers = field.getModifiers();
 assertFalse("Field should be static", Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers));
 Class genericType = GenericMaster.getGenericType(field);
 assertEquals(String.class, genericType);
 } catch (NoSuchFieldException e) {
 throw new AssertionError(e);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullField() {
 Class<?> ungenericClass = TestClass.class;
 Class genericType = GenericMaster.getGenericType(null);
 }
}