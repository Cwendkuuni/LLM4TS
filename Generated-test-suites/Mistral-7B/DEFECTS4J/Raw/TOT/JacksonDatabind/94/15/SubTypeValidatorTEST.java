package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Parameterized.class)
public class SubTypeValidatorTest {

 private static final ObjectMapper mapper = new ObjectMapper();
 private static SubTypeValidator validator = SubTypeValidator.instance();

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Test valid classes
 {"java.lang.String"},
 {"java.util.HashMap"},
 // Test classes in DEFAULT_NO_DESER_CLASS_NAMES
 {"org.apache.commons.collections.functors.InvokerTransformer"},
 {"org.springframework.beans.factory.ObjectFactory"},
 {"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl"},
 // Test classes starting with PREFIX_SPRING
 {"org.springframework.beans.factory.config.PropertyPathFactoryBean"},
 {"org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor"},
 // Test other classes
 {"com.mchange.v2.c3p0.ComboPooledDataSource"},
 {"com.mchange.v2.c3p0.debug.AfterCloseLoggingComboPooledDataSource"},
 {"com.fasterxml.jackson.databind.JsonNode"}
 });
 }

 @BeforeClass
 public static void setup() {
 // Do nothing
 }

 @Test
 public void testValidTypes(JavaType type) throws JsonProcessingException, IOException {
 validate(type, true);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypes(JavaType type) throws JsonProcessingException, IOException {
 validate(type, false);
 }

 private void validate(JavaType type, boolean expectedResult) throws JsonProcessingException, IOException {
 DeserializationContext ctxt = mapper.getDeserializationContext();
 try {
 validator.validateSubType(ctxt, type);
 if (expectedResult) {
 assertTrue("Valid type passed validation", true);
 }
 } catch (JsonMappingException e) {
 if (!expectedResult) {
 assertTrue("Invalid type failed validation", true);
 } else {
 throw e;
 }
 }
 }
}