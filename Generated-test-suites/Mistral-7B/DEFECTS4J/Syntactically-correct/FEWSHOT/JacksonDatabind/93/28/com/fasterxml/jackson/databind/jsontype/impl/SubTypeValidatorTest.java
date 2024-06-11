package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private static final SubTypeValidator INSTANCE = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeWithValidType() {
 JavaType validType = new ClassType(JavaType.constructUnboundedType(String.class));
 INSTANCE.validateSubType(new DeserializationContext(null, null, null), validType);
 }

 @Test
 public void testValidateSubTypeWithEmptyType() {
 assertNull(INSTANCE.validateSubType(new DeserializationContext(null, null, null), null));
 }

 @Test
 public void testValidateSubTypeWithInvalidType1() {
 JavaType invalidType1 = new ClassType(JavaType.constructUnboundedType(SubTypeValidator.class));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType1));
 }

 @Test
 public void testValidateSubTypeWithInvalidType2() {
 JavaType invalidType2 = new ClassType(JavaType.constructUnboundedType("org.apache.commons.collections4.functors.InstantiateTransformer"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType2));
 }

 @Test
 public void testValidateSubTypeWithInvalidType3() {
 JavaType invalidType3 = new ClassType(JavaType.constructUnboundedType("org.springframework.beans.factory.config.PropertyPathFactoryBean"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType3));
 }

 @Test
 public void testValidateSubTypeWithInvalidType4() {
 JavaType invalidType4 = new ClassType(JavaType.constructUnboundedType("java.util.logging.FileHandler"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType4));
 }

 @Test
 public void testValidateSubTypeWithInvalidType5() {
 JavaType invalidType5 = new ClassType(JavaType.constructUnboundedType("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType5));
 }

 @Test
 public void testValidateSubTypeWithInvalidType6() {
 JavaType invalidType6 = new ClassType(JavaType.constructUnboundedType("com.sun.rowset.JdbcRowSetImpl"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType6));
 }

 @Test
 public void testValidateSubTypeWithInvalidType7() {
 JavaType invalidType7 = new ClassType(JavaType.constructUnboundedType("java.rmi.server.UnicastRemoteObject"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType7));
 }

 @Test
 public void testValidateSubTypeWithInvalidType8() {
 JavaType invalidType8 = new ClassType(JavaType.constructUnboundedType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource"));
 assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(new DeserializationContext(null, null, null), invalidType8));
 }
}