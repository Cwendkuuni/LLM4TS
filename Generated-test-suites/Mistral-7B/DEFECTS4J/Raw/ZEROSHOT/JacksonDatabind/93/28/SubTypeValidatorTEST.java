package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

 @InjectMocks
 private SubTypeValidator subTypeValidator;

 private DeserializationContext context;
 private JavaType type;

 @Before
 public void setUp() {
 context = Mockito.mock(DeserializationContext.class);
 type = Mockito.mock(JavaType.class);
 }

 @Test
 public void testValidateSubTypeWithValidType() throws JsonMappingException {
 String validClassName = "com.example.ValidType";
 subTypeValidator.validateSubType(context, type);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubTypeWithNastyClass() throws JsonMappingException {
 String nastyClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 subTypeValidator.validateSubType(context, Mockito.argThat(javaType -> javaType.getRawClass().getName().equals(nastyClassName)));
 // JsonMappingException should be thrown
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, type));
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
 String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 subTypeValidator.validateSubType(context, Mockito.argThat(javaType -> javaType.getRawClass().getName().equals(springClassName)));
 // JsonMappingException should be thrown
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, type));
 }

 @Test
 public void testValidateSubTypeWithSubclassOfNastyClass() throws JsonMappingException {
 String nastySubclassName = "com.example.SubclassOfNastyType";
 Class<?> nastyClass = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
 Class<?> subclass = Class.forName(nastySubclassName);
 JavaType javaType = Mockito.mock(JavaType.class);
 Mockito.when(javaType.getRawClass()).thenReturn(subclass);
 subTypeValidator.validateSubType(context, javaType);
 // JsonMappingException should be thrown
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, type));
 }

 @Test
 public void testValidateSubTypeWithClassStartingWithSpringPrefix() throws JsonMappingException {
 String springPrefixedClassName = "org.springframework.MyClass";
 subTypeValidator.validateSubType(context, Mockito.argThat(javaType -> javaType.getRawClass().getName().startsWith(SubTypeValidator.PREFIX_STRING)));
 // No exception should be thrown
 }

 @Test
 public void testGetInstance() throws JsonMappingException {
 SubTypeValidator validator1 = SubTypeValidator.instance();
 SubTypeValidator validator2 = SubTypeValidator.instance();
 assertEquals(validator1, validator2);
 }
}