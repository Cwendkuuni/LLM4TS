package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDescriptor;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test file for SubTypeValidator class.
 */
public class SubTypeValidatorTest {

    @Test
    public void testValidSubTypes() {
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType validType = TypeDescriptor.value(String.class);
        validator.validateSubType(createContext(), validType);

        JavaType invalidType1 = TypeDescriptor.value(Object[].class);
        JavaType invalidType2 = TypeDescriptor.value(int[].class);
        JavaType invalidType3 = TypeDescriptor.construct(String[].class, Integer.TYPE);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType2));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), invalidType3));

        // Verify that no exception is thrown for valid subtypes
        JavaType validSubType1 = TypeDescriptor.construct(String.class, "ValidSubType");
        JavaType validSubType2 = TypeDescriptor.construct(Integer.class, "ValidSubTypeInt");
        JavaType validSubType3 = TypeDescriptor.construct(Double.class, "ValidSubTypeDouble");

        validator.validateSubType(createContext(), validSubType1);
        validator.validateSubType(createContext(), validSubType2);
        validator.validateSubType(createContext(), validSubType3);
    }

    @Test
    public void testSpringNastyClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with a valid class
        JavaType validType = TypeDescriptor.value(String.class);
        validator.validateSubType(createContext(), validType);

        // Test with a nasty Spring class
        JavaType nastyType = TypeDescriptor.value(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType));
    }

    @Test
    public void testIllegalClassNamesCheck() {
        SubTypeValidator validator = new SubTypeValidator() {
            // Override illegal class names set for testing
            protected Set<String> _cfgIllegalClassNames = new HashSet<>();
            {
                _cfgIllegalClassNames.add("com.example.MyClass");
            }
        };

        JavaType illegalType = TypeDescriptor.value(validator.getClass());
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), illegalType));
    }

    private DeserializationContext createContext() {
        return new DeserializationContext(null, JavaTypeFactory.getDefaultInstance(), new Object(), Arrays.asList(), Collections.emptyMap(), Arrays.asList(SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }
}