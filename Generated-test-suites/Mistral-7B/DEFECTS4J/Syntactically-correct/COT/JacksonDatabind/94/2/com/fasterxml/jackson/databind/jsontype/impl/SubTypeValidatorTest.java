package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test cases for SubTypeValidator class
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException, JsonProcessingException {
        JavaType type = MAPPER.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_BlacklistedType() throws JsonMappingException, JsonProcessingException {
        JavaType type = MAPPER.getTypeFactory().constructType(InvokerTransformer.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), type);
    }

    @Test
    public void testValidateSubType_SpringType() throws JsonMappingException, JsonProcessingException {
        JavaType type = MAPPER.getTypeFactory().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_SpringBlacklistedType() throws JsonMappingException, JsonProcessingException {
        JavaType type = MAPPER.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), type);
    }

    @Test
    public void testValidateSubType_EmptySet() {
        Set<String> emptySet = Collections.emptySet();
        SubTypeValidator validator = new SubTypeValidator();
        validator._cfgIllegalClassNames = emptySet;
        JavaType type = MAPPER.getTypeFactory().constructType(String.class);
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_NullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(MAPPER.getConfig()), null);
    }
}