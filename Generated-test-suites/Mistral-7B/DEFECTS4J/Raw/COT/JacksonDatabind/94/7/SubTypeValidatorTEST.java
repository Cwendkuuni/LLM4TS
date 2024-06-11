package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_legalType() {
 JavaType legalType = JavaType.construct(int[].class);
 validator.validateSubType(null, legalType);
 }

 @Test
 public void testValidateSubType_illegalTypeStartsWithPrefixSpring() {
 JavaType illegalType = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal type");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_illegalTypeIsInterface() {
 JavaType illegalType = JavaType.construct(Comparable.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal interface type");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_illegalTypeIsSubclassOfSpringAbstractApplicationContext() {
 JavaType illegalType = JavaType.construct(MySpringApplicationContext.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal type subclass of Spring AbstractApplicationContext");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_illegalTypeIsSubclassOfSpringAbstractPointcutAdvisor() {
 JavaType illegalType = JavaType.construct(MySpringAbstractPointcutAdvisor.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal type subclass of Spring AbstractPointcutAdvisor");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_illegalTypeIsSpringBeanFactory() {
 JavaType illegalType = JavaType.construct(org.springframework.beans.factory.ObjectFactory.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal type Spring BeanFactory");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testValidateSubType_illegalTypeIsSpringDataSource() {
 JavaType illegalType = JavaType.construct(com.mchange.v2.c3p0.ComboPooledDataSource.class);
 try {
 validator.validateSubType(null, illegalType);
 fail("Validation should have failed for illegal type Spring DataSource");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 public static class MySpringApplicationContext extends org.springframework.context.ApplicationContext {
 }

 public static class MySpringAbstractPointcutAdvisor extends org.springframework.aop.support.AbstractPointcutAdvisor {
 }
}