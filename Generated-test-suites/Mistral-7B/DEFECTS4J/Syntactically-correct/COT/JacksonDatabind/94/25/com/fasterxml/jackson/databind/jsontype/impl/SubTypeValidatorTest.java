package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidClass() {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());
 validator.validateSubType(context, validType);
 }

 @Test
 public void testValidateSubType_NastyClass() {
 Set<String> classNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 Class<?> nastyClass = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
 JavaType nastyType = TypeFactory.defaultInstance().constructType(nastyClass);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, nastyType));
 assertEquals("Illegal type (org.apache.commons.collections4.functors.InstantiateTransformer) to deserialize: prevented for security reasons", context.getParsingContext().getParsingException().getMessage());
 }

 @Test
 public void testValidateSubType_SpringClass() {
 Class<?> springClass = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType springType = TypeFactory.defaultInstance().constructType(springClass);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, springType));
 }

 @Test
 public void testValidateSubType_Interface() {
 JavaType interfaceType = TypeFactory.defaultInstance().constructType(Runnable.class);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

 validator.validateSubType(context, interfaceType);
 }

 @Test
 public void testValidateSubType_EmptySet() {
 Set<String> emptySet = Collections.emptySet();
 SubTypeValidator validator = new SubTypeValidator();
 validator._cfgIllegalClassNames = emptySet;

 JavaType emptySetType = TypeFactory.defaultInstance().constructType(HashSet.class);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

 validator.validateSubType(context, emptySetType);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidateSubType_NullType() {
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());
 validator.validateSubType(context, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateSubType_NullContext() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);

 assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(null, validType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateSubType_NullTypeName() {
 SubTypeValidator validator = SubTypeValidator.instance();
 Class<?> nullType = null;

 assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory()), nullType));
 }

 @Test
 public void testValidateSubType_CommonClasses() {
 Set<String> classNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 Arrays.stream(new Class[] {
         "org.apache.commons.collections.functors.InvokerTransformer",
         "org.apache.commons.collections4.functors.InstantiateTransformer",
         "org.codehaus.groovy.runtime.ConvertedClosure",
         "org.codehaus.groovy.runtime.MethodClosure",
         "org.springframework.beans.factory.ObjectFactory",
         "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
         "org.apache.xalan.xsltc.trax.TemplatesImpl",
         "com.sun.rowset.JdbcRowSetImpl",
         "java.util.logging.FileHandler",
         "java.rmi.server.UnicastRemoteObject"
 }).forEach(className -> {
         try {
             Class<?> clazz = Class.forName(className);
             JavaType type = TypeFactory.defaultInstance().constructType(clazz);
             DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

             validator.validateSubType(context, type);
             fail("Expected JsonMappingException for the class: " + clazz.getName());
         } catch (ClassNotFoundException e) {
             // ignore
         } catch (JsonMappingException ex) {
             // expected
         }
     });
 }

 @Test
 public void testValidateSubType_SpringClasses() {
 Set<String> classNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 Arrays.stream(new Class[] {
         Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean"),
         Class.forName("org.apache.tomcat.dbcp.dbcp2.BasicDataSource")
 }).forEach(className -> {
         try {
             JavaType type = TypeFactory.defaultInstance().constructType(className);
             DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

             validator.validateSubType(context, type);
             fail("Expected JsonMappingException for the class: " + className.getName());
         } catch (ClassNotFoundException e) {
             // ignore
         } catch (JsonMappingException ex) {
             // expected
         }
     });
 }

 @Test
 public void testValidateSubType_CustomClass() {
 Set<String> classNames = new HashSet<>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 String customClassName = "com.custom.NastyClass";
 Class<?> customClass = null;
 try {
     customClass = Class.forName(customClassName);
 } catch (ClassNotFoundException e) {
     throw new RuntimeException("Class not found: " + customClassName, e);
 }
 JavaType customType = TypeFactory.defaultInstance().constructType(customClass);
 DeserializationContext context = new DeserializationContext(new DefaultSerializers.Base(), new JavaTypeFactory());

 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, customType));
 }
}