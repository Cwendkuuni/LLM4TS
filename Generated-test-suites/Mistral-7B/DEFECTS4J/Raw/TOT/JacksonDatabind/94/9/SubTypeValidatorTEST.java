package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() throws Exception {
 Class<String> type = String.class;
 DeserializationContext context = null; // not used here
 validator.validateSubType(context, type.getType());
 }

 @Test
 public void testSpringApplicationContext() throws Exception {
 Class<AbstractApplicationContext> type = AbstractApplicationContext.class;
 DeserializationContext context = null;
 validator.validateSubType(context, type.getType());
 }

 @Test
 public void testSpringPointcutAdvisor() throws Exception {
 Class<AbstractPointcutAdvisor> type = AbstractPointcutAdvisor.class;
 DeserializationContext context = null;
 validator.validateSubType(context, type.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClass() throws Exception {
 Class<org.apache.commons.collections4.functors.InstantiateTransformer> type = InstantiateTransformer.class;
 DeserializationContext context = null;
 validator.validateSubType(context, type.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testJndiRefForwardingDataSource() throws Exception {
 Class<com.mchange.v2.c3p0.JndiRefForwardingDataSource> type = JndiRefForwardingDataSource.class;
 DeserializationContext context = null;
 validator.validateSubType(context, type.getType());
 }

 @Test(expected = JsonMappingException.class)
 public void testComboPooledDataSource() throws Exception {
 Class<com.mchange.v2.c3p0.ComboPooledDataSource> type = ComboPooledDataSource.class;
 DeserializationContext context = null;
 validator.validateSubType(context, type.getType());
 }
}