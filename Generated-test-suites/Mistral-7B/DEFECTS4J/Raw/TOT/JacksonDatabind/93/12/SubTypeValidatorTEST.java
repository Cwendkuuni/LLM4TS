package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

 private DeserializationContext context;
 private SubTypeValidator validator;

 @Before
 public void setup() {
 context = createContext();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidClass() throws Exception {
 JavaType validType = context.constructType(String.class);
 validator.validateSubType(context, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClass() throws Exception {
 JavaType nastyType = context.constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 validator.validateSubType(context, nastyType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClassWithSubclass() throws Exception {
 JavaType springType = context.constructType(AbstractApplicationContext.class);
 validator.validateSubType(context, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClass() throws Exception {
 JavaType springType = context.constructType(AbstractPointcutAdvisor.class);
 validator.validateSubType(context, springType);
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(new AnnotatedClass(SubTypeValidator.class), null, null);
 }

}