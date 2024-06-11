package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for SubTypeValidator.
 */
public class SubTypeValidatorTest {
    private SubTypeValidator validator;
    private DeserializationContext context;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
        context = new TestContext();
        typeFactory = context.getTypeFactory();
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        JavaType validType = typeFactory.constructType(String.class);
        validator.validateSubType(context, validType);
    }

    @Test
    public void testValidateSubType_BlacklistedStringType() throws JsonMappingException {
        JavaType blacklistedType = typeFactory.constructType(SubTypeValidator.class.getName());
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, blacklistedType));
    }

    @Test
    public void testValidateSubType_BlacklistedClassWithSubclass() throws JsonMappingException, IllegalAccessException, InstantiationException, InvocationTargetException {
        JavaType blacklistedType = typeFactory.constructType(Object[].class);
        JavaType subclassType = typeFactory.constructType(String[].class);
        JavaType testType = typeFactory.constructType(Arrays.asList(blacklistedType, subclassType).toArray(new JavaType[0]));

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, testType));
    }

    @Test
    public void testValidateSubType_SpringTypeWithSuperclass() throws JsonMappingException {
        JavaType springType = typeFactory.constructType(AbstractApplicationContext.class);
        assert validator.validateSubType(context, springType);
    }

    @Test
    public void testValidateSubType_SpringType() throws JsonMappingException {
        JavaType springType = typeFactory.constructType(AbstractPointcutAdvisor.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, springType));
    }

    @Test
    public void testValidateSubType_UnknownClass() throws JsonMappingException {
        JavaType unknownType = typeFactory.constructType("com.unknown.package.UnknownClass");
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, unknownType));
    }

    /**
     * Custom DeserializationContext implementation for unit tests.
     */
    static class TestContext extends DeserializationContext {
        @Override
        public void failContextual(String msg, Throwable t) {
            throw new RuntimeException(msg, t);
        }

        @Override
        public void failWithMessage(String msg) {
            throw new RuntimeException(msg);
        }
    }
}