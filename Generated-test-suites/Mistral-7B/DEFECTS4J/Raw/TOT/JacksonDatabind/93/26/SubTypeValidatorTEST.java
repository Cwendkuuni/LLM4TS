package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidType() throws Exception {
 ClassIdType idType = TypeFactory.defaultInstance().constructTypeId(String.class);
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testInvalidTypeWithPrefix() throws Exception {
 Class<AbstractApplicationContext> testClass = AbstractApplicationContext.class;
 JavaType type = mapper.getTypeFactory().constructType(testClass);
 try {
 validator.validateSubType(mapper.getDeserializationContext(), type);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException ex) {
 // Expected
 }
 }

 @Test
 public void testInvalidTypeWithoutPrefix() throws Exception {
 Class<String> testClass = String.class;
 JavaType type = mapper.getTypeFactory().constructType(testClass);
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test
 public void testInvalidTypeFromHashSet() throws Exception {
 String illegalClassName = "org.apache.commons.collections.functors.InstantiateTransformer";
 Set<String> illegalClassNames = new HashSet<>();
 illegalClassNames.add(illegalClassName);
 SubTypeValidator instanceWithCustomList = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = illegalClassNames;
 };
 JavaType type = mapper.getTypeFactory().constructType(Class.forName(illegalClassName));
 try {
 instanceWithCustomList.validateSubType(mapper.getDeserializationContext(), type);
 fail("Expected JsonMappingException");
 } catch (ClassNotFoundException e) {
 // Expected
 } catch (JsonMappingException ex) {
 // Expected
 }
 }

 @Test
 public void testValidTypeFromHashSet() throws Exception {
 String validClassName = "java.lang.String";
 Set<String> illegalClassNames = new HashSet<>();
 illegalClassNames.add("org.apache.commons.collections.functors.InstantiateTransformer");
 SubTypeValidator instanceWithCustomList = new SubTypeValidator() {
 @Override
 protected Set<String> _cfgIllegalClassNames = illegalClassNames;
 };
 JavaType type = mapper.getTypeFactory().constructType(Class.forName(validClassName));
 instanceWithCustomList.validateSubType(mapper.getDeserializationContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testIllegalTypeWithPrefixAndSubclass() throws Exception {
 Class<AbstractPointcutAdvisor> testClass = AbstractPointcutAdvisor.class;
 JavaType type = mapper.getTypeFactory().constructType(testClass);
 validator.validateSubType(mapper.getDeserializationContext(), type);
 }
}