package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class SubTypeValidatorTest {

 private DeserializationContext ctxt;
 private JavaTypeFactory typeFactory;
 private SubTypeValidator validator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 ctxt = new DeserializationContext(new StandardDeserializer(StringDeserializer.instance), new DeserializationProblemHandler(), null);
 typeFactory = ctxt.getTypeFactory();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubTypeWithValidTypes() throws JsonMappingException {
 JavaType validType1 = typeFactory.constructType(String.class);
 JavaType validType2 = typeFactory.constructType(Integer.class);
 JavaType validType3 = typeFactory.constructType(ClassIdType.class);
 validator.validateSubType(ctxt, validType1);
 validator.validateSubType(ctxt, validType2);
 validator.validateSubType(ctxt, validType3);
 }

 @Test
 public void testValidateSubTypeWithInvalidTypes() throws JsonMappingException {
 JavaType invalidType1 = typeFactory.constructType(SubTypeValidator.class);
 JavaType invalidType2 = typeFactory.constructType(DeserializationContext.class);
 JavaType invalidType3 = typeFactory.constructType(Object[].class);
 JavaType invalidType4 = typeFactory.constructType(NullValueProvider.class);
 JavaType invalidType5 = typeFactory.constructType(DeserializationProblemHandler.class);
 JavaType invalidType6 = typeFactory.constructType(BeanDeserializerBase.class);

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType2));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType3));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType4));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType5));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType6));
 }

 @Test
 public void testValidateSubTypeWithSpringClasses() throws JsonMappingException {
 JavaType springType1 = typeFactory.constructType("org.springframework.beans.factory.ObjectFactory");
 JavaType springType2 = typeFactory.constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType springType3 = typeFactory.constructType("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
 JavaType springType4 = typeFactory.constructType("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
 JavaType springType5 = typeFactory.constructType("javax.naming.Context"); // Excluded from DEFAULT_NO_DESER_CLASS_NAMES

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType2));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType3));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType4));
 }
}