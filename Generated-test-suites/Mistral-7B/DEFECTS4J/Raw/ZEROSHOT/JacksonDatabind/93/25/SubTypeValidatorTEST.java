package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

 @InjectMocks
 private SubTypeValidator subTypeValidator;

 @Test
 public void testValidateSubTypeWithValidType() {
 JavaType validType = Mockito.mock(JavaType.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);

 subTypeValidator.validateSubType(context, validType);
 }

 @Test
 public void testValidateSubTypeWithIllegalType() {
 String illegalClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType illegalType = Mockito.mock(JavaType.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Mockito.when(illegalType.getRawClass().getName()).thenReturn(illegalClassName);

 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, illegalType));
 }

 @Test
 public void testValidateSubTypeWithSpringType() {
 String springClassName = "org.springframework.beans.factory.ObjectFactory";
 JavaType springType = Mockito.mock(JavaType.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Mockito.when(springType.getRawClass().getName()).thenReturn(springClassName);

 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, springType));
 }
}