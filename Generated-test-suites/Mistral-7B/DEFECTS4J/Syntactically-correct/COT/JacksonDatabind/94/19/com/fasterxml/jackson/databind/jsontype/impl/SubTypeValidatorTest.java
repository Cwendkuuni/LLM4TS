package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test class for {@link SubTypeValidator}
 */
public class SubTypeValidatorTest {

 private static final Set<String> DEFAULT_NO_DESER_CLASS_NAMES = new HashSet<String>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_names());

 @Test
 public void testValidateSubType_validClass() throws JsonMappingException {
 String className = "com.example.MyValidClass";
 JavaType type = new ClassType(MyValidClass.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mockDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_invalidClass() throws JsonMappingException {
 String className = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType type = new ClassType(PropertyPathFactoryBean.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockDeserializationContext(), type));
 }

 @Test
 public void testValidateSubType_springInterface() throws JsonMappingException {
 String className = "org.springframework.beans.factory.config.AbstractApplicationContext";
 JavaType type = new ClassType(AbstractApplicationContext.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mockDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_springClass() throws JsonMappingException {
 String className = "org.springframework.beans.factory.ObjectFactory";
 JavaType type = new ClassType(ObjectFactory.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockDeserializationContext(), type));
 }

 @Test
 public void testValidateSubType_springSuperclassChain() throws JsonMappingException {
 String className = "org.springframework.beans.factory.config.AbstractPointcutAdvisor";
 JavaType type = new ClassType(AbstractPointcutAdvisor.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mockDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_customIllegalClassName() throws JsonMappingException {
 String customIllegalClassName = "com.example.CustomIllegalClass";
 JavaType type = new ClassType(CustomIllegalClass.class);
 SubTypeValidator validator = new SubTypeValidator();
 validator._cfgIllegalClassNames = new HashSet<String>(Arrays.asList(customIllegalClassName));
 validator.validateSubType(mockDeserializationContext(), type);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockDeserializationContext(), type));
 }

 @Test
 public void testValidateSubType_emptyClassName() throws JsonMappingException {
 String emptyClassName = "";
 JavaType type = new ClassType(Object.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mockDeserializationContext(), type);
 }

 @Test
 public void testValidateSubType_nullClassName() throws JsonMappingException {
 JavaType type = new ClassType(Object.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(mockDeserializationContext(), type));
 }

 private static DeserializationContext mockDeserializationContext() {
 return new TestDeserializationContext();
 }

 private static class TestDeserializationContext extends DeserializationContext {
 @Override
 public AnnotatedClass findClass(ClassIdResolver idResolver, JavaType type) {
 return new AnnotatedClass(type, null, Collections.emptyMap(), null);
 }

 @Override
 public AnnotatedMember findMember(ClassIdResolver idResolver, JavaType type, String name) {
 return new AnnotatedMember(type.getRawClass().getDeclaredMethod(name), null, Collections.emptyMap());
 }
 }
}