package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getField("elementData");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = String.class;
 Class actualType = GenericMaster.getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 Class clazz = String[].class;
 Class actualType = GenericMaster.getGenericType(null);
 Assert.assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getField("length");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actualType = GenericMaster.getGenericType(field);
 Assert.assertEquals(Integer.TYPE, actualType);
 }

 @Test
 public void testGetGenericType_nonParameterizedType() {
 Class clazz = List.class;
 Field field;
 try {
 field = clazz.getField("class");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actualType = GenericMaster.getGenericType(field);
 Assert.assertEquals(Class.class, actualType);
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericType_nonExistentField() {
 Class clazz = String[].class;
 GenericMaster.getGenericType(null);
 }
}