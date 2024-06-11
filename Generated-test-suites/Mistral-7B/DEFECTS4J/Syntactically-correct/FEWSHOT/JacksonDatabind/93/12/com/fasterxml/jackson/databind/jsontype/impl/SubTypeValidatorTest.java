package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private static final SubTypeValidator INSTANCE = SubTypeValidator.instance();

 @Test
 public void testValidationWithValidTypes() {
     JavaType validType1 = getTypeOf("java.lang.String");
     JavaType validType2 = getTypeOf("com.example.MyCustomType");
     INSTANCE.validateSubType(mockContext(), validType1);
     INSTANCE.validateSubType(mockContext(), validType2);
 }

 @Test
 public void testValidationWithIllegalTypeStartingWithPrefix() {
     JavaType illegalType = getTypeOf("org.springframework.beans.factory.config.PropertyPathFactoryBean");
     assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(mockContext(), illegalType));
 }

 @Test
 public void testValidationWithIllegalTypeInSet() {
     JavaType illegalType = getTypeOf("org.apache.commons.collections4.functors.InstantiateTransformer");
     assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(mockContext(), illegalType));
 }

 @Test
 public void testValidationWithIllegalSuperclass() {
     JavaType illegalType = getTypeOf("com.example.MyParentType");
     Class<?> illegalSuperclass = getClassOf("com.example.MyNastyChildType");
     illegalType.setRawClass(illegalSuperclass);
     assertThrows(JsonMappingException.class, () -> INSTANCE.validateSubType(mockContext(), illegalType));
 }

 private JavaType getTypeOf(String className) {
     JavaType type = null;
     try {
         type = new JavaType(getClass().getCLASS_loader(), className);
     } catch (Exception e) {
         throw new RuntimeException(e);
     }
     return type;
 }

 private Class<?> getClassOf(String className) {
     try {
         return Class.forName(className);
     } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
     }
 }

 private DeserializationContext mockContext() {
     return new DeserializationContext(null, Collections.emptyMap(), null);
 }

}