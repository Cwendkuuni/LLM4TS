package com.example.subtypevalidator;

import static org.junit.Assert. assertThrows;
import static org.junit.Assert. assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test class for SubTypeValidator.
 */
public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testNoDeserializationClasses() {
 Set<String> illegalClassNames = validator._cfgIllegalClassNames;

 assertEquals(29, illegalClassNames.size());

 Class<?>[] knownIllegalClasses = new Class<?>[] {
 // Courtesy of [https://github.com/kantega/notsoserial]:
 new Class<?>[] {
 "org.apache.commons.collections.functors.InvokerTransformer",
 "org.apache.commons.collections.functors.InstantiateTransformer",
 "org.apache.commons.collections4.functors.InvokerTransformer",
 "org.apache.commons.collections4.functors.InstantiateTransformer",
 "org.codehaus.groovy.runtime.ConvertedClosure",
 "org.codehaus.groovy.runtime.MethodClosure",
 "org.springframework.beans.factory.ObjectFactory",
 "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
 "org.apache.xalan.xsltc.trax.TemplatesImpl",
 // [databind#1680]: may or may not be problem, take no chance
 "com.sun.rowset.JdbcRowSetImpl",
 // [databind#1737]; JDK provided
 "java.util.logging.FileHandler",
 "java.rmi.server.UnicastRemoteObject",
 // [databind#1737]; 3rd party
 "org.apache.tomcat.dbcp.dbcp2.BasicDataSource"
 },
 // Added for testing
 new Class<?>[] { "com.example.test.TestClass" }
 };

 for (Class<?> illegalClass : knownIllegalClasses) {
 String className = illegalClass.getName();
 assertEquals(className, illegalClassNames.toString().substring(1, illegalClassNames.toString().length() - 1).split(",")[0]);
 }
 }

 @Test
 public void testValidateSubType_ValidType() {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(null, validType);
 }

 @Test
 public void testValidateSubType_IllegalType_SpringFramework() {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, illegalType));
 }

 @Test
 public void testValidateSubType_IllegalType_CustomClass() {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(TestClass.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, illegalType));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIllegalMethod() {
 validator._cfgIllegalClassNames = new HashSet<>();
 }
}