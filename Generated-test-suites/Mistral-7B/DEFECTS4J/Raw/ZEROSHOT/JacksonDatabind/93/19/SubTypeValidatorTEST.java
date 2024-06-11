package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;

 @Test
 public void testValidType() throws JsonProcessingException, JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(String.class);
 instance.validateSubType(ctxt, type);
 // No exception should be thrown
 }

 @Test
 public void testInvalidTypeWithPrefixString() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
 }

 @Test
 public void testInvalidTypeWithIllegalClassName() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(InvokerTransformer.class);
 assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
 }

 @Test
 public void testValidateSubTypeWithNestedClass() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(NestedClass.class);
 assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
 }

 @Test
 public void testValidateSubTypeWithInterface() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(Serializable.class);
 instance.validateSubType(ctxt, type);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubTypeWithAbstractClass() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(AbstractClass.class);
 assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
 }

 @Test
 public void testValidateSubTypeWithAnnotatedClass() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(AnnotatedClass.class);
 instance.validateSubType(ctxt, type);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubTypeWithPackageName() throws JsonMappingException {
 SubTypeValidator instance = SubTypeValidator.instance();
 DeserializationContext ctxt = mock(DeserializationContext.class);
 JavaType type = mock(JavaType.class);

 when(type.getRawClass()).thenReturn(packageName.MyClass.class);
 assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
 }

 private static class AbstractClass {
 }

 private static class AnnotatedClass {
 }

 private static class NestedClass {
 }

 private static class packageName {
 static class MyClass {
 }
 }
}