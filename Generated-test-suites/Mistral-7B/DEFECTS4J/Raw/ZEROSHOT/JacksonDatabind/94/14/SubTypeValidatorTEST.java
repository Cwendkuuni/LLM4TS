package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testDefaultNoDeserClassNames() {
 Set<String> actual = validator._cfgIllegalClassNames;
 Set<String> expected = SubTypeValidator.DEFAULT_NO_DESER_CLASS_Names;
 assertEquals("Sets must be equal", expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidationWithNastyClassName() throws Exception {
 JavaType type = createJavaType("java.lang.Object", "org.apache.commons.collections4.functors.InstantiateTransformer");
 validator.validateSubType(createContext(), type);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testValidationWithSpringNastyClassName() throws Exception {
 JavaType type = createJavaType("java.lang.Object", "org.springframework.beans.factory.config.PropertyPathFactoryBean");
 validator.validateSubType(createContext(), type);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testValidationWithSpringAbstractPointcutAdvisor() throws Exception {
 JavaType type = createJavaType("java.lang.Object", "org.springframework.beans.factory.config.AbstractPointcutAdvisor");
 validator.validateSubType(createContext(), type);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testValidationWithSpringApplicationContext() throws Exception {
 JavaType type = createJavaType("java.lang.Object", "org.springframework.context.ApplicationContext");
 validator.validateSubType(createContext(), type);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testValidationWithValidClassName() throws Exception {
 JavaType type = createJavaType("java.lang.Object", "java.lang.String");
 validator.validateSubType(createContext(), type);
 }

 private DeserializationContext createContext() {
 DeserializationContext ctxt = mock(DeserializationContext.class);
 when(ctxt.getTypeFactory()).then(new TypeDeserializer.TypeDeserializerProvider() {
 @Override
 public JavaTypeFactory getTypeFactory() {
 return mock(JavaTypeFactory.class);
 }
 }).getTypeFactory();
 return ctxt;
 }

 private JavaType createJavaType(Class<?> raw, String subTypeName) {
 JavaTypeFactory factory = mock(JavaTypeFactory.class);
 when(factory.constructType(eq(raw), any(TypeSerializer.class))).thenReturn(new TypeReference<JavaType>() {}.getType(factory, raw, String.class));
 TypeSerializer serializer = mock(TypeSerializer.class);
 when(serializer.deserializeTypedFromString(anyString(), any(DeserializationContext.class))).thenReturn(JavaType.construct(JavaType.constructRawType(subTypeName), factory));
 return TypeSerializer.instance().deserializeTypedFromString("", createContext(), JavaType.constructRawType(raw.getName()), serializer);
 }
}