package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClassName() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType allowedType = TypeDeserializer.findContentType(new ClassIdResolver() {
            @Override
            public Class<?> resolveClass(int id) {
                return String.class;
            }
        }, null);
        validator.validateSubType(new TestContext(), allowedType);
    }

    @Test
    public void testValidateSubTypeWithSpringClassNameWithoutSubclassCheck() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType notAllowedType = TypeDeserializer.findContentType(new ClassIdResolver() {
            @Override
            public Class<?> resolveClass(int id) {
                return org.springframework.core.env.PropertySource.class;
            }
        }, null);
        validator.validateSubType(new TestContext(), notAllowedType);
    }

    @Test
    public void testValidateSubTypeWithSpringClassNameWithSubclassCheck() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType notAllowedParentType = TypeDeserializer.findContentType(new ClassIdResolver() {
            @Override
            public Class<?> resolveClass(int id) {
                return org.springframework.core.env.PropertySource.class;
            }
        }, null);
        JavaType notAllowedType = TypeDeserializer.findContentType(new ClassIdResolver() {
            @Override
            public Class<?> resolveClass(int id) {
                return org.springframework.core.env.MapPropertySource.class;
            }
        }, notAllowedParentType);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new TestContext(), notAllowedType));
    }

    @Test
    public void testValidateSubTypeWithForbiddenClassName() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType forbiddenType = TypeDeserializer.findContentType(new ClassIdResolver() {
            @Override
            public Class<?> resolveClass(int id) {
                return com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class;
            }
        }, null);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new TestContext(), forbiddenType));
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(Collections.emptySet(), new ObjectMapper(), null, null, null, null, new TypeDeserializer[0], new ObjectReader[0], null, null, null);
        }
    }
}