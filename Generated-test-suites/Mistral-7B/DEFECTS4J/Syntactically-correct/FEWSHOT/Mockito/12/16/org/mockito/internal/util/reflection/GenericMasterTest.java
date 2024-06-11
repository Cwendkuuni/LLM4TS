package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Field field = getFieldExample();
 Class genericType = new GenericMaster().getGenericType(field);

 assertEquals(String.class, genericType);
 }

 @Test
 public void testGetGenericType_whenFieldIsNotGeneric_thenReturnObjectClass() {
 Field field = getNonGenericFieldExample();
 Class genericType = new GenericMaster().getGenericType(field);

 assertEquals(Object.class, genericType);
 }

 @Test
 public void testGetGenericType_whenFieldIsArrayType_thenReturnArrayClass() {
 Field field = getArrayFieldExample();
 Class genericType = new GenericMaster().getGenericType(field);

 assertEquals(int[].class, genericType);
 }

 @Test
 public void testGetGenericType_whenNestedGenerics_thenReturnOuterGenericType() {
 Field field = getNestedGenericFieldExample();
 Class genericType = new GenericMaster().getGenericType(field);

 assertEquals(List.class, genericType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_whenFieldIsNull_thenThrowNullPointerException() {
 Class genericType = new GenericMaster().getGenericType(null);
 }

 private Field getFieldExample() {
 try {
 return SomeClass.class.getField("field");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getNonGenericFieldExample() {
 try {
 return SomeClass.class.getField("nonGenericField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getArrayFieldExample() {
 try {
 return SomeClass.class.getField("arrayField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private Field getNestedGenericFieldExample() {
 try {
 return NestedClass.class.getField("listField");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 static class SomeClass {
 private String field;
 private int nonGenericField;
 private int[] arrayField;
 }

 static class NestedClass {
 private List listField;
 }
}