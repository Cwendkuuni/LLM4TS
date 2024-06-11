package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = null;
 try {
 Field field = SomeClass.class.getField("fieldName");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = String.class;
 Class actualType = GenericMaster.getGenericType(field);

 assertEquals("testGetGenericType: expected type != actual type", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 Class actualType = GenericMaster.getGenericType(null);

 assertNull("testGetGenericType_nullField: actual type should be null", actualType);
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Field field = null;
 try {
 field = SomeClass.class.getField("nonGenericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actualType = GenericMaster.getGenericType(field);

 assertEquals("testGetGenericType_nonGenericField: expected type = Object.class", Object.class, actualType);
 }

 @Test
 public void testGetGenericType_singleLevelGenericField() {
 Field field = null;
 try {
 field = SomeClassWithGenericField.class.getField("genericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Integer.class;
 Class actualType = GenericMaster.getGenericType(field);

 assertEquals("testGetGenericType_singleLevelGenericField: expected type = actual type", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_multipleLevelGenericField() {
 Field field = null;
 try {
 field = SomeClassWithNestedGenericField.class.getField("nestedGenericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = String.class;
 Class actualType = GenericMaster.getGenericType(field);

 assertEquals("testGetGenericType_multipleLevelGenericField: expected type = actual type", expectedType, actualType);
 }
}