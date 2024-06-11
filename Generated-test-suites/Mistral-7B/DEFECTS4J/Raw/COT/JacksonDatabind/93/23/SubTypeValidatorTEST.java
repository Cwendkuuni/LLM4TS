package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test file for SubTypeValidator class
 */
public class SubTypeValidatorTest {

    private static SubTypeValidator validator;

    @BeforeClass
    public static void setup() {
        validator = SubTypeValidator.instance();
    }

    // Test for the public constructor
    @Test
    public void testConstructor() {
        assert null != validator;
    }

    // Test for the getInstance() method
    @Test
    public void testInstance() {
        assertSame(validator, SubTypeValidator.instance());
    }

    // Test for the validateSubType() method with a valid type
    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        JavaType validType = new JavaType(String.class);
        validator.validateSubType(null, validType);
    }

    // Test for the validateSubType() method with an invalid type - class in the list of "nasty classes"
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType_NastyClass() throws JsonMappingException, NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        JavaType invalidType = new JavaType(InvokerTransformer.class);
        validator.validateSubType(null, invalidType);
    }

    // Test for the validateSubType() method with an invalid type - class starting with "org.springframework."
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType_SpringFrameworkType() throws JsonMappingException, NoSuchMethodException,
            InstantiationException, InvocationTargetException {
        JavaType invalidType = new JavaType(AbstractApplicationContext.class);
        validator.validateSubType(null, invalidType);
    }

    // Test for the validateSubType() method with an empty type
    @Test(expected = NullPointerException.class)
    public void testValidateSubType_EmptyType() {
        validator.validateSubType(null, null);
    }
}