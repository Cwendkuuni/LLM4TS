package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test class for {@link SubTypeValidator}
 */
public class SubTypeValidatorTest {

 private static SubTypeValidator validator;
 private static Set<String> defaultNoDeserClassNameSet;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 defaultNoDeserClassNameSet = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a valid class name
 */
 @Test
 public void testValidClassName() throws JsonProcessingException {
 String validClassFullName = "com.example.valid.ValidClass";
 JavaType validType = new JavaType(validator.getDeserializationContext().getConstructableContext(), validClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), validType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with an illegal class name
 */
 @Test(expected = JsonMappingException.class)
 public void testIllegalClassName() throws JsonProcessingException {
 String illegalClassFullName = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType illegalType = new JavaType(validator.getDeserializationContext().getConstructableContext(), illegalClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), illegalType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a Spring class name
 */
 @Test
 public void testSpringClass() throws JsonProcessingException {
 String springClassFullName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springType = new JavaType(validator.getDeserializationContext().getConstructableContext(), springClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), springType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a Spring class name that should be denied
 */
 @Test(expected = JsonMappingException.class)
 public void testSpringClassToBeDenied() throws JsonProcessingException {
 String springClassFullName = "org.springframework.beans.factory.ObjectFactory";
 JavaType springType = new JavaType(validator.getDeserializationContext().getConstructableContext(), springClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), springType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a class name that starts with a prefix string
 */
 @Test
 public void testClassStartsWithPrefixString() throws JsonProcessingException {
 String springClassFullName = "org.springframework.beans.factory.AbstractApplicationContext";
 JavaType springType = new JavaType(validator.getDeserializationContext().getConstructableContext(), springClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), springType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a class name that is in the default illegal class names set
 */
 @Test(expected = JsonMappingException.class)
 public void testIllegalClassNameInDefaultSet() throws JsonProcessingException {
 String illegalClassFullName = "org.apache.commons.collections.functors.InvokerTransformer";
 JavaType illegalType = new JavaType(validator.getDeserializationContext().getConstructableContext(), illegalClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), illegalType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a class name that should be allowed despite starting with a prefix string
 */
 @Test
 public void testClassAllowedDespitePrefixString() throws JsonProcessingException {
 String springClassFullName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springType = new JavaType(validator.getDeserializationContext().getConstructableContext(), springClassFullName);
 validator.validateSubType(validator.getDeserializationContext(), springType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * when the illegal class names set is empty
 */
 @Test(expected = AssertionError.class)
 public void testIllegalClassNameSetEmpty() {
 Set<String> emptyIllegalClassNameSet = new HashSet<>();
 SubTypeValidator emptyValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = emptyIllegalClassNameSet;
 };
 emptyValidator.validateSubType(validator.getDeserializationContext(), validType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * when the illegal class names set is modified
 */
 @Test(expected = AssertionError.class)
 public void testIllegalClassNameSetModified() {
 Set<String> modifiableIllegalClassNameSet = Collections.unmodifiableSet(defaultNoDeserClassNameSet);
 modifiableIllegalClassNameSet.add("new.ClassName");
 SubTypeValidator modifiableValidator = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = modifiableIllegalClassNameSet;
 };
 JavaType newClassNameType = new JavaType(validator.getDeserializationContext().getConstructableContext(), "new.ClassName");
 assertThrows(JsonMappingException.class, () -> modifiableValidator.validateSubType(validator.getDeserializationContext(), newClassNameType));
 }
}