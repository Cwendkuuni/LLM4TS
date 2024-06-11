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
 field = clazz.getDeclaredField("value");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Integer[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for Field 'value' in Class 'String[]' ", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NullField() {
 Class actualType = new GenericMaster().getGenericType(null);

 assertEquals("Test getGenericType method for null field", Object.class, actualType);
 }

 @Test
 public void testGetGenericType_NonGenericField() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getDeclaredField("valueOf");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for non-generic field", Object.class, actualType);
 }

 @Test
 public void testGetGenericType_ArrayField() {
 Class clazz = new int[0][].getClass();
 Field field;
 try {
 field = clazz.getDeclaredField("value");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = int[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for Array field", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_ListField() {
 Class clazz = ArrayList.class;
 try {
 Field field = clazz.getDeclaredField("elementData");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for List field", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_MapField() {
 Class clazz = HashMap.class;
 try {
 Field field = clazz.getDeclaredField("table");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Map.Entry[].class;
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for Map field", expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NestedGenericField() {
 Class clazz = ArrayList.class;
 try {
 Field field = clazz.getDeclaredField("element");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object.class; //Expected Object as we don't go deep for nested generics
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals("Test getGenericType method for nested generic field", expectedType, actualType);
 }
}