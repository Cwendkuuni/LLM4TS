package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() {
 JavaType validType = TypeIdResolver.naturalId(JavaType.getType(String.class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return String.class;
 }
 });
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testNastyClass() {
 JavaType nastyClass = TypeIdResolver.naturalId(JavaType.getType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 });
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyClass));
 }

 @Test
 public void testNastySpringClass() {
 JavaType nastySpringClass = TypeIdResolver.naturalId(JavaType.getType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 });
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastySpringClass));
 }

 @Test
 public void testNastyClassWithSuperClassCheck() {
 JavaType nastyClass = TypeIdResolver.naturalId(JavaType.getType(AbstractPointcutAdvisor.class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 });
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyClass));
 }

 @Test
 public void testAllowedClass() {
 JavaType allowedClass = TypeIdResolver.naturalId(JavaType.getType(String[].class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 });
 validator.validateSubType(createContext(), allowedClass);
 }

 @Test
 public void testPrefixStringClass() {
 JavaType prefixStringClass = TypeIdResolver.naturalId(JavaType.getType(AbstractApplicationContext.class), new ClassIdResolver() {
 @Override
 public Class<?> resolveClassId(int id) {
 return null;
 }
 });
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), prefixStringClass));
 }

 private DeserializationContext createContext() {
 return new TestContext();
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public JavaType findTypeById(int id) {
 return null;
 }
 }

}