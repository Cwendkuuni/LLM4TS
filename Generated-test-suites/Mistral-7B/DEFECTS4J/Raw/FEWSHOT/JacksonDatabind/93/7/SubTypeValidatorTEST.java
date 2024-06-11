package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private static final SubTypeValidator SUB_TYPE_VALIDATOR = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeWithValidClass() throws JsonMappingException {
 JavaType validType = JavaType.construct(int[].class);
 SUB_TYPE_VALIDATOR.validateSubType(DeserializationContext.empty(), validType);
 }

 @Test
 public void testValidateSubTypeWithInvalidClassStartingWithPrefix() throws JsonMappingException, JsonProcessingException {
 JavaType invalidType = JavaType.construct(AbstractPointcutAdvisor.class);
 DeserializationContext context = new DeserializationContext(new TestAnnotations(), new TestSerializerProvider());
 SUB_TYPE_VALIDATOR.validateSubType(context, invalidType);
 // The following line can throw JsonMappingException
 assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, invalidType));
 }

 @Test
 public void testValidateSubTypeWithInvalidClassEndingWithBannedSuffix() throws JsonMappingException {
 JavaType invalidType = JavaType.construct(AbstractBeanFactoryPointcutAdvisor.class);
 DeserializationContext context = new DeserializationContext(new TestAnnotations(), new TestSerializerProvider());
 SUB_TYPE_VALIDATOR.validateSubType(context, invalidType);
 // The following line can throw JsonMappingException
 assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, invalidType));
}

 @Test
 public void testValidateSubTypeWithNullJavaType() {
 assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(DeserializationContext.empty(), null));
 }

 @Test
 public void testValidateSubTypeWithBannedClassName() throws JsonMappingException {
 JavaType bannedType = JavaType.construct(InvokerTransformer.class);
 DeserializationContext context = new DeserializationContext(new TestAnnotations(), new TestSerializerProvider());
 assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, bannedType));
 }

 @Test
 public void testValidateSubTypeWithBannedClassNameWithSuperclasses() throws JsonMappingException, JsonProcessingException {
 JavaType bannedType = JavaType.construct(InvokerTransformer.class);
 DeserializationContext context = new DeserializationContext(new TestAnnotations(), new TestSerializerProvider());
 // The following line can throw JsonMappingException
 assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, bannedType));

 // Verify that the error message contains the correct class name
 String expectedErrorMessage = "Illegal type (org.apache.commons.collections4.functors.InvokerTransformer) to deserialize: prevented for security reasons";
 JsonMappingException exception = assertThrows(JsonMappingException.class, () -> SUB_TYPE_VALIDATOR.validateSubType(context, bannedType));
 assertEquals(expectedErrorMessage, exception.getMessage());
 }
}