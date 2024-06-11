package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private static final String PREFIX_STRING = "org.springframework.";

 @InjectMocks
 private SubTypeValidator subTypeValidator;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testValidateSubTypeWithAllowedClass() throws JsonMappingException {
 JavaType allowedType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 subTypeValidator.validateSubType(context, allowedType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubTypeWithDisallowedClass() throws JsonMappingException {
 JavaType disallowedType = TypeFactory.defaultInstance().constructType(InvokerTransformer.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 subTypeValidator.validateSubType(context, disallowedType);
 // JsonMappingException should be thrown
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, disallowedType));
 }

 @Test
 public void testValidateSubTypeWithSubclassOfDisallowedClass() throws JsonMappingException {
 JavaType subclassOfDisallowedType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 subTypeValidator.validateSubType(context, subclassOfDisallowedType);
 // No exception should be thrown
 }

 @Test
 public void testValidateSubTypeWithClassStartingWithSpringPrefix() throws JsonMappingException {
 JavaType springClass = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 subTypeValidator.validateSubType(context, springClass);
 // JsonMappingException should be thrown
 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(context, springClass));
 }
}