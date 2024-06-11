package com.yourpackage.test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;
 private static Set<String> defaultNoDeserClassName;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 defaultNoDeserClassName = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 }

 // Test method for constructor SubTypeValidator()
 @Test
 public void testSubTypeValidator() {
 // No action needed since the constructor is empty
 }

 // Test method for public static SubTypeValidator instance()
 @Test
 public void testInstance() {
 SubTypeValidator instance = SubTypeValidator.instance();
 assertEquals(validator, instance);
 }

 // Test method for public void validateSubType(DeserializationContext, JavaType)
 @Test
 public void testValidateSubType() {
 // Test with a safe class
 JavaType safeType = new JavaType(String.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), safeType));

 // Test with a forbidden class
 JavaType forbiddenType = new JavaType(Object.class.getName() + "$$spring$AbstractApplicationContext");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), forbiddenType));

 // Test with a interface
 JavaType interfaceType = new JavaType(Object.class, Object.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), interfaceType));

 // Test with a Spring class
 JavaType springType = new JavaType(SubTypeValidator.class.getPackage().getName() + ".SubTypeValidator");
 JavaType springSubType = new JavaType(SubTypeValidator.class.getPackage().getName() + ".SubTypeValidator$$Spring");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springType));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springSubType));

 // Test with a forbidden Spring class
 JavaType springForbiddenType = new JavaType(SubTypeValidator.class.getPackage().getName() + ".AbstractApplicationContext");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springForbiddenType));
 }

 // Test edge cases and exceptions
 @Test
 public void testEdgeCasesAndExceptions() {
 // Test with an empty class name
 assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(new DeserializationContext(null), new JavaType(new String[]{}, String.class)));

 // Test with a null DeserializationContext
 assertThrows(NullPointerException.class, () -> validator.validateSubType(null, new JavaType(String.class)));

 // Test with a null JavaType
 assertThrows(NullPointerException.class, () -> validator.validateSubType(new DeserializationContext(null), null));

 // Test with an empty JavaType
 JavaType emptyType = new JavaType(new String[0], String.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), emptyType));

 // Test with an invalid class name format
 JavaType invalidType = new JavaType(new String[]{"com.yourpackage.invalid"}, String.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), invalidType));

 // Test with a cyclic class
 JavaType cyclicType1 = new JavaType(SubTypeValidator.class, SubTypeValidator.class);
 JavaType cyclicType2 = new JavaType(SubTypeValidator.class, SubTypeValidator.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), cyclicType1));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), cyclicType2));

 // Test with a class not in the forbidden list
 Set<String> customNoDeserClassName = new HashSet<>(defaultNoDeserClassName);
 customNoDeserClassName.add("com.yourpackage.SafeClass");
 SubTypeValidator customValidator = new SubTypeValidator();
 customValidator._cfgIllegalClassNames = customNoDeserClassName;
 assertThrows(JsonMappingException.class, () -> customValidator.validateSubType(new DeserializationContext(null), new JavaType(String.class)));
 }
}