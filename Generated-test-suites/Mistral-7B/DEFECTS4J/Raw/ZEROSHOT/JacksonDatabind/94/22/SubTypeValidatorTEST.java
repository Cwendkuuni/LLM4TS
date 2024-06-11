package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static Set<String> defaultNoDeserClassName;
 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 defaultNoDeserClassName = new HashSet<String>();
 // Initialize the default set of invalid classes
 defaultNoDeserClassName.add("org.apache.commons.collections.functors.InvokerTransformer");
 defaultNoDeserClassName.add("org.apache.commons.collections4.functors.InvokerTransformer");
 defaultNoDeserClassName.add("org.apache.commons.collections4.functors.InstantiateTransformer");
 defaultNoDeserClassName.add("org.codehaus.groovy.runtime.ConvertedClosure");
 defaultNoDeserClassName.add("org.codehaus.groovy.runtime.MethodClosure");
 defaultNoDeserClassName.add("org.springframework.beans.factory.ObjectFactory");
 defaultNoDeserClassName.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
 defaultNoDeserClassName.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
 defaultNoDeserClassName.add("com.sun.rowset.JdbcRowSetImpl");
 defaultNoDeserClassName.add("java.util.logging.FileHandler");
 defaultNoDeserClassName.add("java.rmi.server.UnicastRemoteObject");
 // Add more 3rd party classes as needed
 defaultNoDeserClassName.add("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
 defaultNoDeserClassName.add("com.sun.org.apache.bcel.internal.util.ClassLoader");

 SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES = Collections.unmodifiableSet(defaultNoDeserClassName);
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidSubType() throws JsonMappingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = new DeserializationContext(null, JavaTypeFactory.defaultInstance());
 validator.validateSubType(context, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType() throws JsonMappingException {
 JavaType invalidType = TypeFactory.defaultInstance().constructType(Object.class, String.class);
 DeserializationContext context = new DeserializationContext(null, JavaTypeFactory.defaultInstance());
 validator.validateSubType(context, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClassWithAbstractPointcutAdvisor() throws JsonMappingException {
 JavaType springType = TypeFactory.defaultInstance()
 .constructType(Object.class, "org.springframework.context.annotation.Configuration" +
 ".ConfigurationClass" +
 ".BeanDefinitionHolder" +
 ".AbstractBeanDefinition" +
 ".AbstractPointcutAdvisor");
 DeserializationContext context = new DeserializationContext(null, JavaTypeFactory.defaultInstance());
 validator.validateSubType(context, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClassWithAbstractApplicationContext() throws JsonMappingException {
 JavaType springType = TypeFactory.defaultInstance()
 .constructType(Object.class, "org.springframework.context.annotation.AnnotationConfigApplicationContext" +
 ".AnnotationConfigApplicationContext" +
 ".AbstractApplicationContext");
 DeserializationContext context = new DeserializationContext(null, JavaTypeFactory.defaultInstance());
 validator.validateSubType(context, springType);
 }

 @Test
 public void testSpringClassWithoutAbstractPointcutAdvisorOrApplicationContext() throws JsonMappingException {
 JavaType springType = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.beans.factory.BeanFactory");
 DeserializationContext context = new DeserializationContext(null, JavaTypeFactory.defaultInstance());
 validator.validateSubType(context, springType);
 }
}