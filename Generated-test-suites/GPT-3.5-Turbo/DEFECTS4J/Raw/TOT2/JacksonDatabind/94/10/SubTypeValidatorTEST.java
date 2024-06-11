package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testInstance() {
        assertEquals(validator, SubTypeValidator.instance());
    }

    @Test
    public void testValidateSubType_WithValidType() {
        // Valid JavaType to test
        JavaType validType = new SimpleType(String.class);

        // Assert that no exception is thrown
        assertDoesNotThrow(() -> validator.validateSubType(mock(DeserializationContext.class), validType));
    }

    @Test
    public void testValidateSubType_WithIllegalClass() {
        // Illegal JavaType to test
        JavaType illegalType = new SimpleType(org.apache.commons.collections.functors.InvokerTransformer.class);

        // Assert that JsonMappingException is thrown
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), illegalType));
    }

    @Test
    public void testValidateSubType_WithSpringFrameworkType() {
        // Spring framework JavaType to test
        JavaType springType = new SimpleType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

        // Assert that no exception is thrown
        assertDoesNotThrow(() -> validator.validateSubType(mock(DeserializationContext.class), springType));
    }

}