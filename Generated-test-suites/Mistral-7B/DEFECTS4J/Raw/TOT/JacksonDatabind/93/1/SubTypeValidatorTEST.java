package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidClass() throws JsonMappingException {
 // Given
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext ctxt = new DeserializationContext(new stdStreamParser(), new ObjectMapper());
 // When
 validator.validateSubType(ctxt, validType);
 // Then
 // Expected no exception
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 // Given
 JavaType nastyType = JavaTypeFactory.instance().constructType(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
 DeserializationContext ctxt = new DeserializationContext(new stdStreamParser(), new ObjectMapper());
 // When & Then
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyType));
 }

 @Test
 public void testSubClassOfNastyClass() throws JsonMappingException {
 // Given
 String parentClass = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
 JavaType parentType = JavaTypeFactory.instance().constructType(Class.forName(parentClass));
 JavaType nestedType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType nestedTypeWithParent = TypeFactory.defaultInstance().constructType(nestedType.getRawClass(), parentType);
 DeserializationContext ctxt = new DeserializationContext(new stdStreamParser(), new ObjectMapper());
 // When & Then
 validator.validateSubType(ctxt, nestedTypeWithParent);
 // Expected no exception
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyInterface() throws JsonMappingException {
 // Given
 JavaType interfaceType = TypeFactory.defaultInstance().constructType(javax.servlet.Filter.class);
 DeserializationContext ctxt = new DeserializationContext(new stdStreamParser(), new ObjectMapper());
 // When & Then
 validator.validateSubType(ctxt, interfaceType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSubclassOfNastyInterface() throws JsonMappingException {
 // Given
 JavaType nestedClassType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType nestedInterfaceType = JavaTypeFactory.instance().constructType(javax.servlet.Filter.class);
 JavaType typeToDeserialize = TypeFactory.defaultInstance().constructType(nestedClassType.getRawClass(), nestedInterfaceType);
 DeserializationContext ctxt = new DeserializationContext(new stdStreamParser(), new ObjectMapper());
 // When & Then
 validator.validateSubType(ctxt, typeToDeserialize);
 }

}