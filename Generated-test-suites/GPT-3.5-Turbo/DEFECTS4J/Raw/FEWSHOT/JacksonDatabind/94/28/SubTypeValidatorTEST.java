package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            validator.validateSubType(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Util.class));
            fail("JsonMappingException not thrown for illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.cli.Util) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        try {
            validator.validateSubType(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(JavaType.class));
            fail("JsonMappingException not thrown for illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.JavaType) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        try {
            validator.validateSubType(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(Test.class));
            fail("JsonMappingException not thrown for illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.junit.Test) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}