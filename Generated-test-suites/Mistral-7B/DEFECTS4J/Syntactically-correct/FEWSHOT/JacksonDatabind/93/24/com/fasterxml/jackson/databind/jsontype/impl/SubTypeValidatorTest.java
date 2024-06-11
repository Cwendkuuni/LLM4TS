package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType allowedType1 = null; // Add a valid JavaType instance here
        JavaType allowedType2 = null; // Add another valid JavaType instance here

        try {
            validator.validateSubType(null, allowedType1);
            validator.validateSubType(null, allowedType2);
        } catch (JsonMappingException e) {
            // We don't expect any exception here
            assertTrue(false);
        }
    }

    @Test
    public void testValidateSubTypeWithDisallowedClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        for (String className : SubTypeValidator.DEFAULT_NO_DESER_CLASS_names) {
            try {
                Class<?> clazz = Class.forName(className);
                JavaType disallowedType = ObjectMapper.findType(clazz);

                try {
                    validator.validateSubType(null, disallowedType);
                    assertTrue(false);
                } catch (JsonMappingException e) {
                    // We expect an exception here
                    String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", className);
                    assertTrue(e.getMessage().equals(expectedMessage));
                }
            } catch (ClassNotFoundException e) {
                // Ignore if the class is not found
            }
        }
    }

    @Test
    public void testValidateSubTypeWithSpringClasses() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Add your test cases for Spring classes here

        // For example:
        String springClassName1 = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        String springClassName2 = "com.mchange.v2.c3p0.JndiRefForwardingDataSource";

        try {
            Class<?> clazz1 = Class.forName(springClassName1);
            JavaType springType1 = ObjectMapper.findType(clazz1);

            try {
                validator.validateSubType(null, springType1);
                assertTrue(false);
            } catch (JsonMappingException e) {
                // We expect an exception here
                String expectedMessage1 = String.format("Illegal type (%s) to deserialize: prevented for security reasons", springClassName1);
                assertTrue(e.getMessage().equals(expectedMessage1));
            }

            Class<?> clazz2 = Class.forName(springClassName2);
            JavaType springType2 = ObjectMapper.findType(clazz2);

            try {
                validator.validateSubType(null, springType2);
                assertTrue(false);
            } catch (JsonMappingException e) {
                // We expect an exception here
                String expectedMessage2 = String.format("Illegal type (%s) to deserialize: prevented for security reasons", springClassName2);
                assertTrue(e.getMessage().equals(expectedMessage2));
            }
        } catch (ClassNotFoundException e) {
            // Ignore if the class is not found
        }
    }

    @Test
    public void testValidateSubTypeWithEmptyType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType emptyType = ObjectMapper.constructType(Collections.emptyList().getClass());

        try {
            validator.validateSubType(null, emptyType);
            assertTrue(false);
        } catch (JsonMappingException e) {
            // We expect an exception here
            String expectedMessage = "Illegal type (java.util.ArrayList) to deserialize: prevented for security reasons";
            assertTrue(e.getMessage().startsWith(expectedMessage));
        }
    }

    @Test
    public void testValidateSubTypeWithNullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType nullType = null;

        try {
            validator.validateSubType(null, nullType);
            assertTrue(false);
        } catch (JsonMappingException e) {
            // We expect an exception here
            String expectedMessage = "java.lang.NullPointerException: cannot deserialize value of type java.lang.Class<java.lang.Void> from ValueReader (no class def found)";
            String actualMessage = e.getCause().getMessage();
            assertTrue(actualMessage.startsWith(expectedMessage));
        }
    }
}