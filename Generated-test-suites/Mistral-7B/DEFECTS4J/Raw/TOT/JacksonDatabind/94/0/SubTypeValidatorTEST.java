package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link SubTypeValidator}.
 */
public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private SubTypeValidator subTypeValidator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 subTypeValidator = SubTypeValidator.instance();
 }

 /**
 * Test valid type.
 */
 @Test
 public void testValidType() throws Exception {
 validate("java.lang.String", null);
 }

 /**
 * Test invalid type with a non-Spring class.
 */
 @Test
 public void testInvalidNonSpringType() throws Exception {
 assertThrows(JsonMappingException.class, () -> {
 validate("org.apache.commons.collections4.functors.InvokerTransformer", null);
 });
 }

 /**
 * Test invalid type with a Spring class.
 */
 @Test
 public void testInvalidSpringType() throws Exception {
 assertThrows(JsonMappingException.class, () -> {
 validate("org.springframework.beans.factory.config.PropertyPathFactoryBean", null);
 });
 }

 /**
 * Test valid type with a prefix.
 */
 @Test
 public void testValidPrefixType() throws Exception {
 validate("org.springframework.beans.factory.config.PropertyEditorRegistry", null);
 }

 /**
 * Test error when _cfgIllegalClassNames is modified.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testIllegalModification() throws Exception {
 _cfgIllegalClassNamesModification();
 }

 private void validate(String className, JavaType type) throws Exception {
 JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 JavaType javaTypeToValidate = objectMapper.getTypeFactory().constructType(className);
 DeserializationContext context = objectMapper.getDeserializationContext();
 subTypeValidator.validateSubType(context, javaTypeToValidate);
 if (type != null) {
 subTypeValidator.validateSubType(context, type);
 }
 }

 private static Set<String> _cfgIllegalClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;
 // This method should not be accessible from the outside
 private void _cfgIllegalClassNamesModification() {
 _cfgIllegalClassNames.add("java.lang.Object");
 }
}