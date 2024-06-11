package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;
 Field field;
 try {
 field = clazz.getDeclaredField("value");
 field.setAccessible(true);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = String.class;
 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getDeclaredField("valueOf");
 field.setAccessible(true);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object.class;
 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_noGenericType() {
 Class clazz = String.class;
 Field field;
 try {
 field = clazz.getDeclaredField("hashCode");
 field.setAccessible(true);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object.class;
 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nestedGenerics() {
 Class clazz = ArrayList.class;
 Field field;
 try {
 field = clazz.getDeclaredField("elementData");
 field.setAccessible(true);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Class expectedType = Object[].class;
 Class actualType = GenericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 Class clazz = String.class;
 Class expectedType = Object.class;
 Class actualType = GenericMaster.getGenericType(null);
 assertEquals(expectedType, actualType);
 }
}