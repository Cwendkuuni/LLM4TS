package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Test class for SubTypeValidator.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

 private DeserializationContext context;
 private SubTypeValidator validator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 context = Mockito.mock(DeserializationContext.class);
 validator = SubTypeValidator.instance();
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test valid case.
 */
 @Test
 public void testValidateSubType_ValidCase() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 validator.validateSubType(context, type);
 // no exception thrown, so test passed
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test case for a class that is in the set of well-known "nasty classes".
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClass() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 String rawClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 Mockito.when(type.getRawClass()).thenReturn(Class.forName(rawClassName));
 validator.validateSubType(context, type);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test case for a class that starts with the prefix "org.springframework." and is not a problem.
 */
 @Test
 public void testValidateSubType_SpringClass_NotProblem() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 String rawClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 Mockito.when(type.getRawClass()).thenReturn(Class.forName(rawClassName));
 validator.validateSubType(context, type);
 // no exception thrown, so test passed
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test case for a class that starts with the prefix "org.springframework." and is a problem.
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass_Problem() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 String rawClassName = "org.springframework.beans.factory.support.AbstractBeanFactoryPointcutAdvisor";
 Mockito.when(type.getRawClass()).thenReturn(Class.forName(rawClassName));
 validator.validateSubType(context, type);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test case for a class that is in the set of well-known "nasty interfaces".
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyInterface() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 String rawClassName = "java.util.logging.FileHandler";
 Mockito.when(type.getRawClass()).thenReturn(Class.forName(rawClassName));
 validator.validateSubType(context, type);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}.
 * Test case for a class that extends a nasty class.
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_ClassThatExtendsNastyClass() throws JsonMappingException {
 JavaType type = Mockito.mock(JavaType.class);
 String rawClassName = "com.example.MyClassThatExtendsNastyClass";
 Mockito.when(type.getRawClass()).thenReturn(Class.forName(rawClassName));
 validator.validateSubType(context, type);
 }
}