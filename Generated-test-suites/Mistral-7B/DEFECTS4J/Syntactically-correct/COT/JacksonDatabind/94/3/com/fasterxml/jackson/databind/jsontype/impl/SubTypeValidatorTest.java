package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for SubTypeValidator
 */
public class SubTypeValidatorTest {

 private static Set<String> _defaultNoDeserClassNames;
 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 Set<String> set = new HashSet<>();
 // Add the default values from the class
 set.addAll(Arrays.asList(
 "org.apache.commons.collections.functors.InvokerTransformer",
 "org.apache.commons.collections4.functors.InvokerTransformer",
 "org.apache.commons.collections4.functors.InstantiateTransformer",
 "org.apache.commons.collections. funeral.InvokerTransformer", // typo added intentionally
 "org.apache.commons.collections4.functors.InstantiateTransformer",
 "org.codehaus.groovy.runtime.ConvertedClosure",
 "org.codehaus.groovy.runtime.MethodClosure",
 "org.springframework.beans.factory.ObjectFactory",
 "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
 "org.apache.xalan.xsltc.trax.TemplatesImpl",
 "com.sun.rowset.JdbcRowSetImpl",
 "java.util.logging.FileHandler",
 "java.rmi.server.UnicastRemoteObject",
 "org.apache.tomcat.dbcp.dbcp2.BasicDataSource"
 ));
 _defaultNoDeserClassNames = Collections.unmodifiableSet(set);
 validator = SubTypeValidator.instance();
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a non-blacklisted class
 */
 @Test
 public void testValidateSubTypeValidClass() {
 JavaType validType = null; // initialization left for reader
 try {
 validator.validateSubType(null, validType);
 } catch (JsonMappingException e) {
 fail("ValidateSubType should not throw an exception for a valid class.");
 }
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a blacklisted class
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeBlacklistedClass() {
 JavaType blacklistedType = new ClassType(Object.class, "org.apache.commons.collections.functors.InvokerTransformer");
 validator.validateSubType(null, blacklistedType);
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a Spring-derived class
 */
 @Test
 public void testValidateSubTypeSpringClass() {
 JavaType springType = new ClassType(Object.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");
 try {
 validator.validateSubType(null, springType);
 fail("ValidateSubType should throw an exception for a Spring class.");
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }

 /**
 * Test method for {@link SubTypeValidator#validateSubType(DeserializationContext, JavaType)}
 * with a typo'ed blacklisted class name
 */
 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeBlacklistedClassTypo() {
 JavaType blacklistedTypeTypo = new ClassType(Object.class, "org.apache.commons.collections.functors.InvokerTransformaer"); // typo added intentionally
 try {
 validator.validateSubType(null, blacklistedTypeTypo);
 fail("ValidateSubType should throw an exception for a typo'ed blacklisted class.");
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }
}