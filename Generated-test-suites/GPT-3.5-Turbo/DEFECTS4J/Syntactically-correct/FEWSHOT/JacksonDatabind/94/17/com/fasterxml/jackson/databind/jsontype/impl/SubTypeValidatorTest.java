
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = new SubTypeValidator();
    }

    @Test
    public void testValidateSubType_WithValidClass() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = context.constructType(String.class);

        try {
            subTypeValidator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("An exception should not have been thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithInvalidClass() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = context.constructType(org.apache.commons.collections.functors.InvokerTransformer.class);

        subTypeValidator.validateSubType(context, type);
    }
}
