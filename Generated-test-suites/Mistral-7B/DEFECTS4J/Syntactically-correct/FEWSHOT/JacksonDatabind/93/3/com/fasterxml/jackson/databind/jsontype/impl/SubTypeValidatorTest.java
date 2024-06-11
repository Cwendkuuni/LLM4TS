package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class SubTypeValidatorTest {

 private SubTypeValidator instance() {
 return SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubTypeWithLegalType() {
 JavaType legalType = TypeIdResolver.simpleValueType(ClassIdType.Int.getRawClass());
 DeserializationContext context = null; // Not used in this test
 instance().validateSubType(context, legalType);
 }

 @Test
 public void testValidateSubTypeWithIllegalTypeStartingWithPrefix() throws JsonMappingException {
 JavaType illegalType = TypeIdResolver.simpleValueType(String.class);
 DeserializationContext context = null; // Not used in this test
 instance().validateSubType(context, illegalType);

 // The following line throws an exception, but assertThrows() suppresses it
 assertThrows(JsonMappingException.class, () -> instance().validateSubType(context, illegalType));
 }

 @Test
 public void testValidateSubTypeWithIllegalTypeContainingPrefixInSuperClassChain() throws JsonMappingException {
 JavaType illegalType = TypeIdResolver.simpleValueType(AbstractPointcutAdvisor.class);
 DeserializationContext context = null; // Not used in this test
 instance().validateSubType(context, illegalType);

 // The following line throws an exception, but assertThrows() suppresses it
 assertThrows(JsonMappingException.class, () -> instance().validateSubType(context, illegalType));
 }

 @Test
 public void testValidateSubTypeWithNullType() {
 DeserializationContext context = null; // Not used in this test
 assertThrows(IllegalArgumentException.class, () -> instance().validateSubType(context, null));
 }
}