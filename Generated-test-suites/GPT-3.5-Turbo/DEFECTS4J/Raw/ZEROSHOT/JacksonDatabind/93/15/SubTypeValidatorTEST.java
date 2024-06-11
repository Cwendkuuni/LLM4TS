package com.fasterxml.jackson.databind.jsontype.impl;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType() {
        type = JavaType.builder().build();
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getMessage(), "Illegal type () to deserialize: prevented for security reasons");
        }
    }
}