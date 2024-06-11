package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 Class clazz = String[].class;

 try {
 Field field = clazz.getField("class");
 assertNotNull(field);
 assertEquals(String.class, GenericMaster.getGenericType(field));

 field = clazz.getField("length");
 assertNotNull(field);
 assertNotNull(field.getGenericType());
 assertEquals(int.class, GenericMaster.getGenericType(field));

 Class nestedListClazz = List.class.getComponentType();
 Class nestedListGenericType = GenericMaster.getGenericType(nestedListClazz.getField("elementType"));
 assertEquals(List.class, nestedListGenericType.getComponentType());
 assertEquals(Object.class, nestedListGenericType.getGenericComponentType());
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericType_nullField() {
 Class clazz = Object.class;
 assertNull(GenericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericType_notGenericField() {
 Class clazz = Object.class;
 try {
 Field field = clazz.getField("hashCode");
 assertEquals(int.class, GenericMaster.getGenericType(field));
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericType_notParameterizedType() {
 Class clazz = Object.class;
 try {
 Field modifiersField = clazz.getField("modifiers");
 modifiersField.setAccessible(true);
 int modifiers = modifiersField.getInt(clazz);
 modifiers |= Modifier.FINAL;
 Field field = clazz.getField("serialVersionUID");
 assertNotNull(field);
 assertEquals(long.class, GenericMaster.getGenericType(field));
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }
}