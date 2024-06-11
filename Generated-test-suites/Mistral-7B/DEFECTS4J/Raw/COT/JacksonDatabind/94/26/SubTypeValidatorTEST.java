package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jedis.JedisDeserializer;

/**
 * Test cases for SubTypeValidator class
 */
public class SubTypeValidatorTest {

 private final static String PREFIX_SPRING = "org.springframework.";

 @Test
 public void testValidateSubType_whenValidType_thenSuccess() throws JsonMappingException, JsonProcessingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = JedisDeserializer.instance().getTypeFactory().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubType_whenIllegalClass_thenExceptionThrown() {
 SubTypeValidator validator = SubTypeValidator.instance();
 Set<String> illegalClasses = new HashSet<String>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
 illegalClasses.add("com.test.unsafe.IllegalClass"); // Add your test illegal class here
 JavaType illegalType = new ClassType(IllegalClass.class);

 try {
 validator.validateSubType(new DeserializationContext(null), illegalType);
 assert false;
 } catch (JsonMappingException e) {
 String actualMessage = e.getMessage();
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", IllegalClass.class.getName());
 assert expectedMessage.equals(actualMessage);
 }
 }

 @Test
 public void testValidateSubType_whenSpringClassWithSubclassThatIsIllegal_thenExceptionThrown() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClass = new ClassType(SpringClassWithIllegalSubclass.class);

 try {
 validator.validateSubType(new DeserializationContext(null), springClass);
 assert false;
 } catch (JsonMappingException e) {
 // SpringClassWithIllegalSubclass is not in the default set of illegal classes, but its subclass is
 // Check the message contains the name of the illegal subclass
 String actualMessage = e.getMessage();
 String expectedMessage = String.format("Illegal type (%s.IllegalSubclass) to deserialize: prevented for security reasons", SpringClassWithIllegalSubclass.class.getName());
 assert expectedMessage.contains(expectedMessage.replace(IllegalSubclass.class.getSimpleName(), "IllegalSubclass"));
 }
 }

 @Test
 public void testValidateSubType_whenNullType_thenExceptionThrown() {
 SubTypeValidator validator = SubTypeValidator.instance();
 try {
 validator.validateSubType(new DeserializationContext(null), null);
 assert false;
 } catch (JsonMappingException e) {
 assert e.getMessage().contains("Null type");
 }
 }
}