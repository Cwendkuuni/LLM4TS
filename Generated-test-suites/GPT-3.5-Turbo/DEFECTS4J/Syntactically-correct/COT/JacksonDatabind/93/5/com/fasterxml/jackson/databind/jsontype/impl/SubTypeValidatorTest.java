
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;
        validator.validateSubType(context, type);
        // No exception should be thrown if the type is valid
    }

}
