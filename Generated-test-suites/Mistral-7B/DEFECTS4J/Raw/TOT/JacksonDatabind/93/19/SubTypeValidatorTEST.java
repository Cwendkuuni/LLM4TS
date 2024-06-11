package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidSubType() throws Exception {
 Class<ValidClass> validType = ValidClass.class;
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validJavaType = MAPPER.getTypeFactory().constructType(validType);
 validator.validateSubType(MAPper.getDeserializationContext(), validJavaType);
 }

 @Test
 public void testInvalidSubTypeWithSimpleName() throws Exception {
 Class<InvalidClass> invalidType = InvalidClass.class;
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidJavaType = MAPPER.getTypeFactory().constructType(invalidType);
 try {
 validator.validateSubType(MAPper.getDeserializationContext(), invalidJavaType);
 fail("Validation should have failed");
 } catch (JsonMappingException e) {
 // Expected
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", InvalidClass.class.getName());
 assertTrue(expectedMessage.equals(e.getMessage()));
 }
 }

 @Test
 public void testInvalidSubTypeWithFullyQualifiedName() throws Exception {
 Class<FullyQualifiedInvalidClass> fullyQualifiedInvalidType = FullyQualifiedInvalidClass.class;
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validJavaType = MAPPER.getTypeFactory().constructType(fullyQualifiedInvalidType);
 try {
 validator.validateSubType(MAPper.getDeserializationContext(), validJavaType);
 fail("Validation should have failed");
 } catch (JsonMappingException e) {
 // Expected
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", FullyQualifiedInvalidClass.class.getName());
 assertTrue(expectedMessage.equals(e.getMessage()));
 }
 }

 @Test
 public void testPrefixStringCheck() throws Exception {
 Class<PrefixStringInvalidClass> prefixStringInvalidType = PrefixStringInvalidClass.class;
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType prefixStringInvalidJavaType = MAPPER.getTypeFactory().constructType(new TypeReference<PrefixStringInvalidClass>() {});
 try {
 validator.validateSubType(MAPper.getDeserializationContext(), prefixStringInvalidJavaType);
 fail("Validation should have failed");
 } catch (JsonMappingException e) {
 // Expected
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", PrefixStringInvalidClass.class.getName());
 assertTrue(expectedMessage.equals(e.getMessage()));
 }
 }
}