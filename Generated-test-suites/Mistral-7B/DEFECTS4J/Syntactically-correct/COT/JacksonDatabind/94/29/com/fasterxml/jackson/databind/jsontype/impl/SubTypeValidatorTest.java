package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 // Test for method: public static SubTypeValidator instance()
 @Test
 public void testInstance() {
 SubTypeValidator instance = SubTypeValidator.instance();
 assertEquals(validator, instance);
 }

 // Test for method: public void validateSubType(DeserializationContext ctxt, JavaType type)
 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType validType = new JavaType(Integer.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_InvalidType_NastyClass() throws JsonMappingException {
 JavaType invalidType = new JavaType(SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testValidateSubType_InvalidType_SpringClass() throws JsonMappingException {
 JavaType invalidType = new JavaType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));
 }

 @Test
 public void testValidateSubType_InvalidType_ThirdPartyClass() throws JsonMappingException {
 JavaType invalidType = new JavaType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));
 }

 // Test for method: protected final static Set<String> DEFAULT_NO_DESER_CLASS_NAMES
 @Test
 public void testDefaultNoDeserClassName_ValidClassName() {
 Set<String> defaultNoDeserClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 assertEquals(true, defaultNoDeserClassName.contains("org.apache.commons.collections.functors.InvokerTransformer"));
 }

 @Test
 public void testDefaultNoDeserClassName_InvalidClassName() {
 Set<String> defaultNoDeserClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 assertThrows(IllegalArgumentException.class, () -> defaultNoDeserClassName.add(new Object()));
 }

 // Test for method: protected Set<String> _cfgIllegalClassNames
 @Test
 public void testCfgIllegalClassNames_AddNewIllegalClassName() {
 Set<String> illegalClassNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 illegalClassNames.add("new.ClassName");
 assertEquals(illegalClassNames.size(), 12 + 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCfgIllegalClassNames_UnmodifiableSet() {
 Set<String> illegalClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 illegalClassNames.clear();
 }

}