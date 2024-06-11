package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_knownNastyClass() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), String.class);
 String nastyClassName = "org.apache.commons.collections.functors.InvokerTransformer";
 validator.validateSubType(new DeserializationContext(null), javaType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), JavaType.construct(getClass(), nastyClassName)));
 }

 @Test
 public void testValidateSubType_notNastyClass() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), String.class);
 validator.validateSubType(new DeserializationContext(null), javaType);
 }

 @Test
 public void testValidateSubType_springFrameworkClass() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), AbstractApplicationContext.class);
 validator.validateSubType(new DeserializationContext(null), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_springFrameworkAbstractPointcutAdvisorClass() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), AbstractPointcutAdvisor.class);
 validator.validateSubType(new DeserializationContext(null), javaType);
 }

 @Test
 public void testValidateSubType_prefixString() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), Object.class);
 validator.validateSubType(new DeserializationContext(null), javaType);

 final JavaType springType = new JavaType(getClass(), AbstractApplicationContext.class);
 validator.validateSubType(new DeserializationContext(null), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_prefixString_springFrameworkAbstractPointcutAdvisorClass() throws JsonMappingException {
 final JavaType javaType = new JavaType(getClass(), Object.class);
 validator.validateSubType(new DeserializationContext(null), javaType);

 final JavaType springType = new JavaType(getClass(), AbstractPointcutAdvisor.class);
 validator.validateSubType(new DeserializationContext(null), springType);
 }

 @Test
 public void testValidateSubType_emptySet() {
 final Set<String> emptySet = Collections.emptySet();
 SubTypeValidator emptyValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = emptySet;
 };
 assertEquals(Collections.emptySet(), emptyValidator.instance()._cfgIllegalClassNames);
 }

 @Test
 public void testValidateSubType_customSet() {
 Set<String> customSet = new HashSet<>();
 Set<String> customValidatorSet = new HashSet<>();
 customSet.add("org.apache.commons.collections.functors.InvokerTransformer");
 customValidatorSet.addAll(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 customValidatorSet.addAll(customSet);

 SubTypeValidator customValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = customValidatorSet;
 };
 assertEquals(customValidatorSet, customValidator.instance()._cfgIllegalClassNames);
 }
}